package org.garden.rest.command;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.garden.domain.dto.PlantRequestDTO;
import org.garden.domain.dto.PlantResponseDTO;
import org.garden.domain.entity.PlantEntity;
import org.garden.mapper.PlantMapper;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/v0/plant_types")
public class PlantTypeResource {

    @Inject
    PlantMapper mapper;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @WithTransaction
    @Retry()
    public Uni<RestResponse<PlantResponseDTO>> post(@NotNull @Valid PlantRequestDTO plant) {

        Log.info("post() API called w/ " + plant);

        PlantEntity entity = mapper.dtoToEntity(plant);
        return entity.<PlantEntity>persist()
                    .map(e -> RestResponse.ResponseBuilder.ok(mapper.entityToDTO(e)).build());
    }
}
