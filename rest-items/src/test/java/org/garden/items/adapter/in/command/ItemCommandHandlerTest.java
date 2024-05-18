package org.garden.items.adapter.in.command;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.TestReactiveTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.garden.items.Utils;
import org.garden.items.configuration.InjectWiremock;
import org.garden.items.configuration.KafkaTestResourceLifecycleManager;
import org.garden.items.configuration.PlantTypeWiremock;
import org.garden.items.domain.constants.PlantState;
import org.garden.items.domain.port.in.CommandHandlerInterface;
import org.garden.items.domain.port.in.ItemService;
import org.garden.items.domain.port.in.commands.CancelItemCommand;
import org.garden.items.domain.port.in.commands.ConfirmCreateItemCommand;
import org.garden.items.domain.port.in.commands.CreateItemCommand;
import org.garden.items.domain.port.out.event.DomainEvent;
import org.garden.items.domain.port.out.event.ItemCreatedDomainEvent;
import org.garden.items.framework.Command;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;

import static org.awaitility.Awaitility.await;


@QuarkusTest
@QuarkusTestResource(KafkaTestResourceLifecycleManager.class)
@QuarkusTestResource(PlantTypeWiremock.class)
class ItemCommandHandlerTest {

    private final String CHANNEL_IN = "SAGA_ITEM_ALLOCATION_EVENT";
    private final String CHANNEL_OUT = "ITEMS_DOMAIN_EVENTS";

    @Inject
    @Connector("smallrye-in-memory")
    InMemoryConnector connector;

    @InjectSpy
    ItemService itemService;

    @InjectSpy
    CommandHandlerInterface itemCommandHandler;


    @InjectWiremock
    WireMockServer wiremock;

    record RandomCommand(
            Long itemId
    ) implements Command {
    }

    @Inject
    Utils utils;

    @BeforeEach
    void setup(UniAsserter uniAsserter) {

        this.wiremock.stubFor(
                get(anyUrl()).willReturn(
                        aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                                .withBody(utils.getDefaultPlantJSON())
                )
        );

        connector.sink(CHANNEL_OUT).clear();

    }


    @Test
    @Order(1)
    public void createNotSupportedCommand() {

        InMemorySource<Command> commandsIn = connector.source(CHANNEL_IN);


        RandomCommand command = new RandomCommand(1L);
        commandsIn.send(command).complete();

        //TODO need to verify the @Spy throws an Exception
        Mockito.verify(itemCommandHandler).handleMessage(any());

    }

    @Test
    @Order(2)
    public void createCommand() {

        InMemorySource<Command> commandsIn = connector.source(CHANNEL_IN);
        InMemorySink<DomainEvent> results = connector.sink(CHANNEL_OUT);

        CreateItemCommand command = new CreateItemCommand(1L);
        commandsIn.runOnVertxContext(true).send(command);

        // Check you have received the expected message
        await().until(results::received, t -> t.size() == 1);

        Mockito.verify(itemCommandHandler).handleMessage(any());
        Mockito.verify(itemService, Mockito.times(1)).create(anyLong());
        Mockito.verify(itemService, never()).cancelCreatedItem(any());
        Mockito.verify(itemService, never()).confirmCreatedItem(any());


        DomainEvent eventResult = results.received().getFirst().getPayload();

        Assertions.assertInstanceOf(ItemCreatedDomainEvent.class, eventResult);
        Assertions.assertArrayEquals(
                ((ItemCreatedDomainEvent) eventResult).getSpaceRequired(),
                new Integer[]{5, 2}
        );


    }

    @Test
    @Order(3)
    @RunOnVertxContext
    @TestReactiveTransaction
    void confirmAllocationCommand(UniAsserter uniAsserter) {

        uniAsserter
                .execute(() -> {
                    InMemorySource<Command> commandsIn = connector.source(CHANNEL_IN);

                    // Step 1 - send the command
                    ConfirmCreateItemCommand command = new ConfirmCreateItemCommand(1L);
                    commandsIn.send(command);

                })
                .execute(() -> {
                    await().until(
                            () -> itemService.getItem(1L)
                                    .subscribe().withSubscriber(UniAssertSubscriber.create())
                                    .awaitItem(Duration.ofSeconds(5))
                                    .getItem(),
                            t -> t.getState() == PlantState.ALLOCATED
                    );


                    Mockito.verify(itemCommandHandler).handleMessage(any());
                    Mockito.verify(itemService, Mockito.times(1)).confirmCreatedItem(1L);
                    Mockito.verify(itemService, never()).create(anyLong());
                    Mockito.verify(itemService, never()).cancelCreatedItem(anyLong());
                });


    }

    @Test
    void test_cancelAllocationCommand() {
        InMemorySource<Command> commandsIn = connector.source(CHANNEL_IN);

        CancelItemCommand command = new CancelItemCommand(1L);
        commandsIn.runOnVertxContext(true).send(command);

        await().atLeast(Duration.ofSeconds(15));

        Mockito.verify(itemService, Mockito.times(1)).cancelCreatedItem(1L);
        Mockito.verify(itemService, never()).create(anyLong());
        Mockito.verify(itemService, never()).confirmCreatedItem(any());


    }


}