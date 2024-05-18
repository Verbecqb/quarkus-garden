package org.garden.items.domain.port.in;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;

import io.smallrye.mutiny.Uni;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.garden.items.domain.port.out.PlantTypeRepository;
import org.garden.items.domain.port.out.event.ItemCreatedDomainEvent;
import org.garden.items.domain.entity.ItemAggregate;
import org.garden.items.adapter.out.persistence.ItemRepository;

import org.garden.items.domain.port.out.ItemDomainEventPublisherInterface;
import org.jboss.logging.Logger;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@ApplicationScoped
public class ItemService {

    private final static Logger logger = Logger.getLogger(ItemService.class);

    @Inject
    ItemRepository itemRepository;

    @Inject
    ItemDomainEventPublisherInterface itemDomainEventPublisher;

    @Inject
    PlantTypeRepository plantTypeClient;

    @WithSession
    public Uni<ItemAggregate> getItem(long id) {
        return itemRepository.findById(id);
    }


    @WithTransaction
    @Retry(
            abortOn = { NoSuchElementException.class },
            maxRetries = 2
    )
    public Uni<ItemAggregate> create(Long plantId) {

        logger.info("Create() service called for plantID: " + plantId);

        // Create item aggregate
        Pair<ItemAggregate, ItemCreatedDomainEvent> res = ItemAggregate.create(plantId);

        ItemAggregate aggregate = res.getLeft();
        ItemCreatedDomainEvent event = res.getRight();

        // Save in DB and publish the event to Kafka
        return Panache.withTransaction(
                    () -> itemRepository.persist(aggregate)
                )
                .onItem().invoke(e -> logger.info("AFTER DB SAVE : >>>>>> " + e))
                .chain(item ->
                        plantTypeClient.get(plantId)
                                .onItem().ifNull().failWith(NoSuchElementException::new)
                                .onItem().invoke(e -> logger.info("AFTER PLANT TYPE Client call : >>>>>> " + e))
                                .chain((plantConfig) -> {
                                    Log.info("setting event data");
                                    aggregate.setId(item.getId());
                                    event.setItemId(item.getId());
                                    event.setFrom(LocalDate.now());
                                    event.setTo(LocalDate.now().plusDays(plantConfig.getGrowingDuration().toDays()));
                                    event.setSpaceRequired(plantConfig.getSpace());
                                    return Uni.createFrom().item(event);
                                }

                ))
                .chain(() -> itemDomainEventPublisher.publish(event))
                .replaceWith(aggregate);
    }

    /**
     * 1. Find the aggregate
     * 2. Aggregate updates its status and returns an event
     * 3. Save
     *
     * @param itemId
     * @return
     */
    @WithTransaction
    public Uni<ItemAggregate> confirmCreatedItem(Long itemId) {

        return itemRepository.findById(itemId)
                .onItem().ifNull().fail()
                .chain((e) -> {
                    e.takeAction().orElseThrow(WebApplicationException::new);
                    return Panache.withTransaction(() ->
                                    itemRepository.persist(e)
                            )
                            .replaceWith(e);
                });


    }

    /**
     * 1. Find the aggregate
     * 2. Aggregate updates its status and returns an event
     * 3. Save
     *
     * @param itemId
     * @return
     */
    @WithTransaction
    public Uni<ItemAggregate> cancelCreatedItem(Long itemId) {

        return itemRepository.findById(itemId)
                .chain((e) -> {
                    e.cancelItem();
                    return Panache.withTransaction(() ->
                                    itemRepository.persist(e)
                            )
                            .onItem().invoke(res -> logger.info("AFTER DB SAVE PUBLISH : >>>>>> " + res))
                            .replaceWith(e);
                })
                .onItem().ifNull().fail();

    }


}
