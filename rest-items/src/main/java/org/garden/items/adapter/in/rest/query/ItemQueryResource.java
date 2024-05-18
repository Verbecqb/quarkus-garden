package org.garden.items.adapter.in.rest.query;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import org.garden.clients.ItemResponseDTO;
import org.garden.items.adapter.in.rest.ItemMapper;
import org.garden.items.adapter.out.persistence.ItemRepository;

import java.util.List;

@Path("/v0/items")
public class ItemQueryResource {

    @Inject
    ItemRepository itemRepository;

    @Inject
    ItemMapper mapper;

    @GET
    public Uni<List<ItemResponseDTO>> getAll() {
        return itemRepository.findAll().list().map(e ->
                e.stream().map(mapper::entityToResponseDTO).toList()
        );

    }



}
