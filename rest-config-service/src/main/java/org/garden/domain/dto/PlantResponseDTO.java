package org.garden.domain.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlantSinglePickUpResponseDTO.class, name = "SINGLE"),
        @JsonSubTypes.Type(value = PlantRecurringPickUpResponseDTO.class, name = "RECURRING")
})
public interface PlantResponseDTO {
}
