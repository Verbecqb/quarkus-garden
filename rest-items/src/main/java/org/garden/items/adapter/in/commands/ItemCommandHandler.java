package org.garden.items.adapter.in.commands;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.garden.items.domain.entity.ItemAggregate;
import org.garden.items.domain.port.in.CommandHandlerInterface;
import org.garden.items.domain.port.in.ItemService;
import org.garden.items.domain.port.in.commands.CancelItemCommand;
import org.garden.items.domain.port.in.commands.ConfirmCreateItemCommand;
import org.garden.items.domain.port.in.commands.CreateItemCommand;
import org.garden.items.framework.Command;
import org.garden.items.framework.CommandHandler;
import org.garden.items.framework.CommandHandlers;

@ApplicationScoped
class ItemCommandHandler implements CommandHandlerInterface {

    private static final String CHANNEL = "SAGA_ITEM_ALLOCATION_EVENT";

    @Inject
    ItemService service;

    CommandHandlers handlers = new CommandHandlers().getBuilder()
                                            .onMessage(CreateItemCommand.class,        this::create)
                                            .onMessage(CancelItemCommand.class,        this::cancelItem)
                                            .onMessage(ConfirmCreateItemCommand.class, this::confirmItem)
                                            .build();

    @Override
    @Incoming(CHANNEL)
    public Uni<ItemAggregate> handleMessage(Command command) {
        Log.info("handle() message called for command: " + command);
        CommandHandler handler = handlers.findTargetMethod(command).orElseThrow(WebApplicationException::new);
        return handler.invokeMethod(command);
    }

    private Uni<ItemAggregate> create(CreateItemCommand c) {
        Log.info("Create() command " + c);
        return this.service.create(c.plantTypeId());
    }

    private Uni<ItemAggregate> cancelItem(CancelItemCommand c) {
        Log.info("cancelItem() command " + c);
        return this.service.cancelCreatedItem(c.itemId());
    }

    private Uni<ItemAggregate> confirmItem(ConfirmCreateItemCommand c) {
        Log.info("confirmItem() command " + c);
        return this.service.confirmCreatedItem(c.itemId());
    }
}


