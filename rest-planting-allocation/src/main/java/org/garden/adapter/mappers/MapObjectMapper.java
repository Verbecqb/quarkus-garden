package org.garden.adapter.mappers;

import org.garden.domain.events.schema.MapObjectCreatedEvent;
import org.garden.domain.port.in.commands.CreateRaisedBedCommand;
import org.garden.adapter.in.rest.dto.CreateRaisedBedRequest;
import org.garden.adapter.in.rest.dto.MapObjectDTO;
import org.garden.domain.entity.MapObject;
import org.garden.domain.entity.RaisedBed;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "cdi", subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface MapObjectMapper {

    @SubclassMapping(source = MapObjectDTO.class, target = RaisedBed.class)
    MapObject dtoToEntity(MapObjectDTO dto);

    CreateRaisedBedCommand requestDTOToCommand(CreateRaisedBedRequest c);

    RaisedBed commandToEntity(CreateRaisedBedCommand c);

    MapObjectCreatedEvent entityToCreatedEvent(MapObject c);
}
