package org.garden.domain.port.in.commands;

import io.smallrye.mutiny.Uni;
import org.garden.domain.events.schema.ItemCreatedEvent;
public interface AllocationCommand {

    Uni<Void> handle(ItemCreatedEvent event);

}
