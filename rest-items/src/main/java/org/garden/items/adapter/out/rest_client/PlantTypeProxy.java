package org.garden.items.adapter.out.rest_client;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.garden.items.domain.port.out.PlantTypeRepository;

@Path("/v0/plant_types")
@RegisterRestClient(configKey = "plant-types-rest-client")
/**
 * Private-package interface for PlantType REST Proxy. Consumer should use {@link  PlantTypeClient}
 */
interface PlantTypeProxy extends PlantTypeRepository {

    @Override
    @GET
    @Path("/{id}")
    Uni<PlantResponseDTO> get(long id);

}
