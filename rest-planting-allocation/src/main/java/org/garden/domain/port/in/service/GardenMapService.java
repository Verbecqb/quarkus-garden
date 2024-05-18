package org.garden.domain.port.in.service;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.garden.domain.port.in.commands.AllocateItemCommand;
import org.garden.domain.port.in.commands.CreateGardenCommand;
import org.garden.domain.port.in.commands.CreateRaisedBedCommand;
import org.garden.domain.entity.GardenMapAggregate;
import org.garden.adapter.mappers.GardenMapper;
import org.garden.adapter.mappers.MapObjectMapper;
import org.garden.adapter.out.DomainEventPublisher;
import org.garden.adapter.out.SagaCommandsEventPublisher;
import org.garden.domain.port.out.repository.GardenMapRepository;

import org.garden.domain.events.schema.*;

import java.util.*;


@ApplicationScoped
public class GardenMapService {

    @Inject
    GardenMapRepository repository;

    @Inject
    DomainEventPublisher publisher;

    @Inject
    SagaCommandsEventPublisher sagaCommandsEventPublisher;

    @Inject
    MapObjectMapper mapper;

    @Inject
    GardenMapper gardenMapper;

    @WithTransaction
    public Uni<GardenMapAggregate> handle(CreateGardenCommand command) {
        return repository.persist(gardenMapper.commandToEntity(command));
    }

    @WithTransaction
    public Uni<GardenMapAggregate> handle(AllocateItemCommand command) {
        return repository.findById(1L) //TODO Manage multiple gardens
                .onItem().ifNull().failWith(NoSuchElementException::new)
                .chain((e) -> {
                    AllocationEvent event = e.allocateItem(command.item_id(), command.space_width(), command.space_length(), command.from(), command.to());
                    return Panache.withTransaction(
                            () -> repository.persist(e)
                    )
                    .invoke(trx -> Log.info("ALLOCATION Item service >>> AFTER GardenAggregate saved"))
                    .chain(
                            () -> sagaCommandsEventPublisher.publish(event)
                    ).replaceWith(e);
                })
                .onFailure().call(() -> {
                        Log.info("handle(AllocateItemCommand command) >>> Could not find space to allocate the seed.");
                        return sagaCommandsEventPublisher.publish(
                                new AllocationEvent(command.item_id(), AllocationStatus.REJECTED)
                        );
                    }
                );
    }

    @WithTransaction
    public Uni<GardenMapAggregate> handle(CreateRaisedBedCommand command) {
        return repository.findById(command.garden_id())
                .onItem().ifNull().failWith(NoSuchElementException::new)
                .chain((e) -> {
                    MapObjectCreatedEvent event = e.addMapObject(mapper.commandToEntity(command));
                    return Panache.withTransaction(
                                () -> repository.persist(e)
                            )
                            .chain(mapObject -> {
                                return publisher.publish(event);
                            })
                            .replaceWith(e);
                });
    }

}
