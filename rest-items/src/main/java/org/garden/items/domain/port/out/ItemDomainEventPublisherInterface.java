package org.garden.items.domain.port.out;

import org.garden.items.domain.port.out.event.DomainEvent;
import io.smallrye.mutiny.Uni;

public interface ItemDomainEventPublisherInterface {

    Uni<Void> publish(DomainEvent domainEvent);

}