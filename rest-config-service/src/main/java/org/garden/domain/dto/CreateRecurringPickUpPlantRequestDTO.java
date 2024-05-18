package org.garden.domain.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Positive;

@JsonTypeName("RECURRING")
public record CreateRecurringPickUpPlantRequestDTO(

        @Positive
        int qty_per_pickup

) implements PlantRequestDTO {
}
