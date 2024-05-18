package org.garden.adapter.in.rest.queries;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.garden.domain.entity.GardenMapAggregate;
import org.garden.adapter.mappers.MapObjectMapper;
import org.garden.domain.port.out.repository.GardenMapRepository;

import java.util.List;

@Path("/v0/map")
public class GardenMapResource {

    @Inject
    MapObjectMapper mapper;

    @Inject
    GardenMapRepository repository;

    @GET
    @WithSession
    public Uni<List<GardenMapAggregate>> getAll() {
        return repository.listAll();
    }

}
