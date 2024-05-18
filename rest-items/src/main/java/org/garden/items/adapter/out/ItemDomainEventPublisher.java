package org.garden.items.adapter.out;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.garden.items.domain.port.out.event.DomainEvent;
import org.garden.items.domain.port.out.ItemDomainEventPublisherInterface;


@ApplicationScoped
class ItemDomainEventPublisher implements ItemDomainEventPublisherInterface {

    private final String ITEM_DOMAIN_EVENTS = "ITEMS_DOMAIN_EVENTS";

    @Inject
    @Channel(ITEM_DOMAIN_EVENTS)
    MutinyEmitter<DomainEvent> emitter;

    @Override
    public Uni<Void> publish(DomainEvent domainEvent) {
        Log.info("PUBLISHING DOMAIN EVENT >>>> Channel: " + ITEM_DOMAIN_EVENTS + " " + domainEvent);
        return emitter.send(domainEvent);
    }

}
