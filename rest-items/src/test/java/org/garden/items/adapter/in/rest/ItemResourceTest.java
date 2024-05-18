package org.garden.items.adapter.in.rest;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.garden.clients.ItemResponseDTO;
import org.garden.items.Utils;
import org.garden.items.adapter.in.rest.command.ItemResource;
import org.garden.items.adapter.out.persistence.ItemRepository;
import org.garden.items.configuration.InjectWiremock;
import org.garden.items.configuration.PlantTypeWiremock;
import org.garden.items.domain.constants.PlantState;
import org.garden.items.domain.entity.ItemAggregate;
import org.garden.items.domain.port.in.ItemService;
import org.garden.items.domain.port.out.ItemDomainEventPublisherInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.mockito.Mockito;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;


@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource.class)
@QuarkusTestResource(PlantTypeWiremock.class)
public class ItemResourceTest {

    final static String ITEMS_DOMAIN_EVENTS = "ITEMS_DOMAIN_EVENTS";

    @InjectWiremock
    WireMockServer wireMockServer;

    @InjectSpy
    ItemRepository itemRepository;

    @InjectSpy
    ItemService itemService;

    @InjectKafkaCompanion
    KafkaCompanion companion;

    @InjectSpy
    ItemDomainEventPublisherInterface itemDomainEventPublisher;

    @Inject
    Utils utils;

    @BeforeEach
    public void prepareData() {
        this.wireMockServer.resetAll();
    }

    private Uni<ItemAggregate> default_aggregate() {
        ItemAggregate itemAggregate = ItemAggregate.create(1L).getLeft();
        return Uni.createFrom().item(itemAggregate);
    }

    @Test
    @TestHTTPEndpoint(ItemResource.class)    
    @RunOnVertxContext
    public void createItemsSuccessTestEndpoint() {

        CreateItemRequestDTO request = new CreateItemRequestDTO(1L);

        when(this.itemService.create(anyLong())).thenReturn(default_aggregate());

        given()
        .when()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .post()
        .then()
            .statusCode(Status.CREATED.getStatusCode());

        //TODO this is called twice - why? Running in IntelliJi works but not when building....
        verify(this.itemService, Mockito.times(1)).create(1L);

    }



    @Test
    @TestHTTPEndpoint(ItemResource.class)
    @Order(1)
    public void postAndValidate( ) {


        // Wiremock 
        this.wireMockServer.stubFor(
                get(anyUrl()).willReturn(
                        aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                                .withBody(utils.getDefaultPlantJSON())
                )
        );

        // Step 1 - Create the HTTP POST Request on ITEM to associate to a PlantType ID = 1
        CreateItemRequestDTO request = new CreateItemRequestDTO(1L);
        ItemResponseDTO response = 
            given()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(request)
            .when().post()
            .then()
                .statusCode(201)
                .extract().as(ItemResponseDTO.class);

        assertNotNull(response);
        

        // Check DB - Check that the item is available in DB
        ItemAggregate fromDB = itemRepository.findById(1L)
            .subscribe().withSubscriber(UniAssertSubscriber.create())
            .assertCompleted()
            .awaitItem(Duration.ofSeconds(4))
            .getItem();

        assertNotNull(fromDB);
        assertEquals(fromDB.getState(), PlantState.AWAITING_ALLOCATION);

        // Check that itemDomainEventPublisher is called once
//        asserter.execute(() -> {
//            Mockito.verify(this.itemDomainEventPublisher, Mockito.times(1)).publish(Mockito.any());
//        });

        companion.consumeStrings().fromTopics("ITEMS_DOMAIN_EVENTS", 1).forEach(e ->
                {
                    System.out.println("here");
                }
        );

        // Check that ItemDomainEventPublisher did publish a message
//        asserter.execute(() ->
//
//                assertEquals(1, companion.consumeStrings().fromTopics("ITEMS_DOMAIN_EVENTS", 1).stream().count())
//        );
//
//
//        // IMPORTANT: We need to execute the asserter within a reactive session
//        asserter.surroundWith(u -> Panache.withSession(() -> u));

    }


    @Test
    @TestHTTPEndpoint(ItemResource.class)
    @RunOnVertxContext
    public void post_items_fails_PlantTypeClientReturns404(UniAsserter asserter) {


        // Test preparation -> stub the HTTP PlantType to return 404
        this.wireMockServer.stubFor(
                get(anyUrl()).willReturn(notFound())
        );

        // Step 1 - Create the HTTP POST Request on ITEM to associate to a PlantType ID = 1
        CreateItemRequestDTO request = new CreateItemRequestDTO(1L);

        given()
                .body(request)
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post()
                .then().assertThat().statusCode(Response.Status.NOT_FOUND.getStatusCode()); // item service will fail as HTTP server for planttype return 404

        // Check that ITEM service is only called ONCE
        Mockito.verify(this.itemService, Mockito.times(1)).create(anyLong());

        // Check DB - Check that the item is available in DB
        asserter.assertNull(() -> itemRepository.findById(1L));

        // Check that itemDomainEventPublisher is called once
        Mockito.verifyNoInteractions(this.itemDomainEventPublisher);

        asserter.execute(() ->
                assertEquals(0, companion.consumeStrings().fromTopics("ITEMS_DOMAIN_EVENTS").stream().count())
        );

        // IMPORTANT: We need to execute the asserter within a reactive session
        asserter.surroundWith(u -> Panache.withSession(() -> u));

    }








}