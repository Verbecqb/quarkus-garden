package org.garden.items.adapter.in.rest;

import org.garden.clients.ItemResponseDTO;
import org.garden.items.domain.entity.ItemAggregate;
import org.garden.items.domain.port.out.event.DomainEvent;
import org.garden.items.domain.port.out.event.ItemCreatedDomainEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface ItemMapper {

    ItemResponseDTO entityToResponseDTO(ItemAggregate e);


    @Mapping(target = "itemId", source = "id")
    ItemCreatedDomainEvent entityToEvent(ItemAggregate e);


}
