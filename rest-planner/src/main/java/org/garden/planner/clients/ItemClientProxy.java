package org.garden.planner.clients;


import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.garden.clients.ItemCreateRequestDTO;
import org.garden.clients.ItemResponseDTO;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/v0/items")
@RegisterRestClient(baseUri = "http://localhost:7806")//(configKey = "item-service-api")
public interface ItemClientProxy {

    @POST
    Uni<RestResponse<ItemResponseDTO>> create_seed_item(@Valid ItemCreateRequestDTO itemDto);


}
