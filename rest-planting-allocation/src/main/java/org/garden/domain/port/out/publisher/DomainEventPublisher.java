package org.garden.domain.port.out.publisher;

import io.smallrye.mutiny.Uni;
import org.garden.domain.events.schema.MapObjectCreatedEvent;

public interface DomainEventPublisher {

    Uni<Void> publish(MapObjectCreatedEvent event);

}
