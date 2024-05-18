package org.garden.clients;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ItemResponseDTO(

        Long id,

        @JsonProperty("plant_type")
        PlantResponseDTO configPlants,

        LocalDate planted_date,

        LocalDate transplanted_date,

        LocalDate pickup_date,

        Integer[] coordinates


) {
}
