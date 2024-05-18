package org.garden.mapper;

import org.garden.domain.dto.*;
import org.garden.domain.entity.PlantEntity;
import org.garden.domain.entity.PlantRecurringPickup;
import org.garden.domain.entity.PlantSinglePickup;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(
        componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        // https://mapstruct.org/documentation/stable/reference/html/#sub-class-mappings
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION
)
public interface PlantMapper {

    @SubclassMapping(source = PlantRecurringPickup.class, target = PlantRecurringPickUpResponseDTO.class)
    @SubclassMapping(source = PlantSinglePickup.class, target = PlantSinglePickUpResponseDTO.class)
    PlantResponseDTO entityToDTO(PlantEntity e);


    @SubclassMapping(source = PlantSinglePickUpRequestDTO.class, target = PlantSinglePickup.class)
    @SubclassMapping(source = CreateRecurringPickUpPlantRequestDTO.class, target = PlantRecurringPickup.class)
    PlantEntity dtoToEntity(PlantRequestDTO dto);

}
