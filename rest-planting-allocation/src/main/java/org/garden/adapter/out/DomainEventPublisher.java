package org.garden.adapter.out;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.garden.domain.events.schema.MapObjectCreatedEvent;

@ApplicationScoped
public class DomainEventPublisher {

    private final String GARDEN_MAP_DOMAIN_EVENTS = "GARDEN_MAP_DOMAIN_EVENTS";

    @Channel(GARDEN_MAP_DOMAIN_EVENTS)
    MutinyEmitter<MapObjectCreatedEvent> emitter;

    public Uni<Void> publish(MapObjectCreatedEvent event) {
        Log.info("DOMAIN Event publishing >>>> " + event);
        return emitter.send(event);
    }

}

