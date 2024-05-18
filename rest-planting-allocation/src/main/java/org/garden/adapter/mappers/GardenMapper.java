package org.garden.adapter.mappers;

import org.garden.domain.port.in.commands.CreateGardenCommand;
import org.garden.domain.entity.GardenMapAggregate;
import org.garden.adapter.in.rest.dto.CreateGardenRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface GardenMapper {

    GardenMapAggregate dtoToEntity(CreateGardenRequest dto);

    CreateGardenCommand requestDTOToCommand(CreateGardenRequest c);

    GardenMapAggregate commandToEntity(CreateGardenCommand c);
}
