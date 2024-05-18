package org.garden.items.adapter.in.rest.command;

import io.smallrye.mutiny.Uni;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.garden.clients.ItemResponseDTO;
import org.garden.items.adapter.in.rest.CreateItemRequestDTO;
import org.garden.items.adapter.in.rest.ItemMapper;

import org.garden.items.domain.port.in.ItemService;
import org.jboss.resteasy.reactive.RestResponse;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;


@Path("/v0/items")
public class ItemResource {

    @Inject
    ItemService itemService;

    @Inject
    ItemMapper mapper;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Create an ItemAggregate",
        description = "Create an item, associate to the plant id with ID passed in body"
    )
    @APIResponse(
        responseCode = "201",
        description = "The item",
        content = @Content(
                mediaType = "text/plain",
                schema = @Schema(implementation = ItemResponseDTO.class)
        )
    )
    @APIResponse(
            responseCode = "404",
            description = "The Plant type is not found for the given identifier"
    )
    public Uni<RestResponse<ItemResponseDTO>> create_item(
            @RequestBody(
                    name = "item",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CreateItemRequestDTO.class)
                    )
            )
            @NotNull @Valid CreateItemRequestDTO createItemRequestDTO) {

        return this.itemService.create(createItemRequestDTO.plant_id())
                .map(e ->
                        RestResponse.ResponseBuilder
                                .ok(mapper.entityToResponseDTO(e))
                                .status(RestResponse.Status.CREATED)
                                .build()
                );
    }





}
