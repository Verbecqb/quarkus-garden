package org.garden.adapter.in.rest.commands;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.garden.domain.port.in.commands.CreateGardenCommand;
import org.garden.domain.port.in.commands.CreateRaisedBedCommand;
import org.garden.domain.entity.GardenMapAggregate;
import org.garden.adapter.in.rest.dto.CreateGardenRequest;
import org.garden.adapter.in.rest.dto.CreateRaisedBedRequest;
import org.garden.adapter.mappers.GardenMapper;
import org.garden.adapter.mappers.MapObjectMapper;
import org.garden.domain.port.in.service.GardenMapService;

@Path("/v0/garden")
public class GardenMapResource {

    @Inject
    MapObjectMapper mapper;

    @Inject
    GardenMapper gardenMapper;

    @Inject
    GardenMapService service;


    @POST
    @Path("/{garden_id}/raised_bed")
    public Uni<GardenMapAggregate> post(@NotNull @Valid CreateRaisedBedRequest request, long garden_id) {
        CreateRaisedBedCommand createRaisedBedCommand = mapper.requestDTOToCommand(request);
        // Associated the garden id
        return service.handle(createRaisedBedCommand.withGardenId(garden_id));
    }

    @POST
    public Uni<GardenMapAggregate> post(@NotNull @Valid CreateGardenRequest request) {
        CreateGardenCommand createGardenCommand = gardenMapper.requestDTOToCommand(request);
        return service.handle(createGardenCommand);
    }



}
