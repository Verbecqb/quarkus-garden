package org.garden.domain.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Size;
import org.garden.constants.PlantType;

import java.time.Duration;
import java.time.Month;
import java.util.Set;

@JsonTypeName("RECURRING")
public record PlantRecurringPickUpResponseDTO(

        PlantType type,

        Long id,

        String name,

        Set<Month> plantingSeasons,

        Duration growingDuration,

        @Size(min = 2, max = 2)
        Integer[] space,

        int qty_per_pickup

) implements PlantResponseDTO {
}
