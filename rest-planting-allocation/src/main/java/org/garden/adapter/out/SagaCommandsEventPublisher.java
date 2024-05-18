package org.garden.adapter.out;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.garden.domain.events.schema.AllocationEvent;

@ApplicationScoped
public class SagaCommandsEventPublisher {

    private final String SAGA_ITEM_ALLOCATION_EVENT = "SAGA_ITEM_ALLOCATION_EVENT";

    @Channel(SAGA_ITEM_ALLOCATION_EVENT)
    MutinyEmitter<AllocationEvent> emitter;

    public Uni<Void> publish(AllocationEvent event) {
        Log.info("SAGA_ITEM_ALLOCATION_EVENT Event publishing >>>> " + event);
        return emitter.send(event);
    }

}

