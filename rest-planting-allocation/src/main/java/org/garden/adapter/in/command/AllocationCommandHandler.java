package org.garden.adapter.in.command;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.garden.domain.port.in.commands.AllocateItemCommand;
import org.garden.domain.port.in.commands.AllocationCommand;
import org.garden.domain.port.in.service.GardenMapService;

@ApplicationScoped
class AllocationCommandHandler implements AllocationCommand {

    private final String ITEM_DOMAIN_EVENTS = "ITEMS_DOMAIN_EVENTS";

    @Inject
    GardenMapService service;

    @Incoming(ITEM_DOMAIN_EVENTS)
    public Uni<Void> handle(org.garden.domain.events.schema.ItemCreatedEvent event) {

        Log.info("EVENT received on channel " + ITEM_DOMAIN_EVENTS + " -- " + event);

        AllocateItemCommand command = new AllocateItemCommand(
                event.getItemId(),
                event.getSpaceRequired().getFirst(), event.getSpaceRequired().get(1),
                event.getFrom(), event.getTo());

        return service.handle(command).replaceWithVoid();
    }
}










