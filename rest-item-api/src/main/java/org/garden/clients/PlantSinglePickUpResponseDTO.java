package org.garden.clients;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Size;

import java.time.Duration;
import java.time.Month;
import java.util.Set;

@JsonTypeName("SINGLE")
public record PlantSinglePickUpResponseDTO(
    PlantType type,
    Long id,
    String name,
    Set<Month> plantingSeasons,
    Duration growingDuration,
    @Size(min = 2, max = 2)
    Integer[] space

) implements PlantResponseDTO { }
