package org.garden.rest.query;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.garden.domain.dto.PlantResponseDTO;
import org.garden.domain.entity.PlantEntity;
import org.garden.mapper.PlantMapper;

import java.util.List;

@Path("/v0/plant_types")
public class PlantTypeQueryResource {

    @Inject
    PlantMapper mapper;

    @GET
    public Uni<List<PlantEntity>> getAll() {
        return PlantEntity.<PlantEntity>findAll().list();
    }

    @GET
    @Path("/{id}")
    public Uni<PlantResponseDTO> get(long id) {
        return PlantEntity.<PlantEntity>findById(id).map(plantEntity -> mapper.entityToDTO(plantEntity));
    }

}
