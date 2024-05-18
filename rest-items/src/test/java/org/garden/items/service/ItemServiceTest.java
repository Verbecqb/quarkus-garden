package org.garden.items.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.NoSuchElementException;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.inject.Inject;
import org.garden.items.Utils;
import org.garden.items.configuration.InjectWiremock;
import org.garden.items.configuration.KafkaTestResourceLifecycleManager;
import org.garden.items.configuration.PlantTypeWiremock;
import org.garden.items.domain.entity.ItemAggregate;
import org.garden.items.adapter.out.persistence.ItemRepository;
import org.garden.items.domain.port.in.ItemService;
import org.garden.items.domain.port.out.PlantTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.smallrye.mutiny.Uni;
import org.mockito.Mockito;

@QuarkusTest
@QuarkusTestResource(KafkaTestResourceLifecycleManager.class)
@QuarkusTestResource(PlantTypeWiremock.class)
public class ItemServiceTest {

    @InjectSpy
    PlantTypeRepository plantTypeRESTClient;

    @InjectWiremock
    WireMockServer wireMockServer;

    @InjectSpy
    ItemService itemService;

    @InjectMock
    ItemRepository itemRepository;


    @BeforeEach
    public void beforeEach() {
        this.wireMockServer.resetAll();
    }

    @Test
    @RunOnVertxContext
    public void testCreateItemPlantTypeClientFails(UniAsserter asserter) {

        this.wireMockServer.stubFor(
                get(anyUrl()).willReturn(notFound())
        );


        asserter.assertFailedWith(
                () -> itemService.create(1L),
                NoSuchElementException.class
        );

        asserter.execute(() ->  {
                    // @Retry on create method should not be trigger in case of NoSuchElementException
                    verify(itemService, Mockito.times(1)).create(anyLong());
                }
        );

    }

    @Test
    @RunOnVertxContext
    public void testCreateItemPlantTypeClientReturnsNull(UniAsserter asserter) {

        asserter.execute(() -> when(itemRepository.persist(any(ItemAggregate.class)))
                .thenReturn(Uni.createFrom().item(ItemAggregate.create(1L).getKey())));

        // REST Client returns NULL
        when(this.plantTypeRESTClient.get(1L)).thenReturn(
                Uni.createFrom().nullItem()
        );

        asserter.assertFailedWith(
                () -> itemService.create(1L),
                NoSuchElementException.class
        );

        // ItemService.create as a Retry of 1 times
        asserter.execute(() -> {
                    verify(plantTypeRESTClient, Mockito.times(1)).get(anyLong());
                }
        );

    }



    @Test
    @RunOnVertxContext
    public void testCreateItemSuccess(UniAsserter asserter) {

        asserter.execute(() -> when(itemRepository.persist(any(ItemAggregate.class)))
                .thenReturn(Uni.createFrom().item(ItemAggregate.create(1L).getKey())));

        when(this.plantTypeRESTClient.get(1L)).thenReturn(
                Uni.createFrom().item(Utils.getDefaultPlantResponseDTO()));

        asserter.assertSame(
                () -> itemService.create(1L).onItem().ifNotNull().transform(ItemAggregate::getId),
                ItemAggregate.create(1L).getKey().getId()
        );

    }

    @Test
    @RunOnVertxContext
    public void testCreateItemPlantTypeClientReturnsTimeoutException(UniAsserter asserter) {

        asserter.execute(() -> when(itemRepository.persist(any(ItemAggregate.class)))
                .thenReturn(Uni.createFrom().item(ItemAggregate.create(1L).getKey())));

        // Delay to greater value than the method's configured timeout 
        when(this.plantTypeRESTClient.get(1L)).thenReturn(
                Uni.createFrom().item(
                                Utils.getDefaultPlantResponseDTO())
                        .invoke(() -> Log.info("Delayed REST Client response"))
                        .onItem()
                        .delayIt()
                        .by(Duration.ofSeconds(1))
        );

        //TODO - Should return only TimeOutException

        asserter.assertSame(
                () -> itemService.create(1L),
                ItemAggregate.create(1L).getKey()
        );


    }


}
