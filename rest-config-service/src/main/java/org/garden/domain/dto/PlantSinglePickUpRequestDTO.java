package org.garden.domain.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.garden.constants.PlantType;

import java.time.Duration;
import java.time.Month;
import java.util.Set;

@JsonTypeName("SINGLE")
public record PlantSinglePickUpRequestDTO (
    PlantType type,

    @NotNull
    String name,

    @NotNull
    @Size(min = 1)
    Set<Month> plantingSeasons,

    @NotNull Duration growingDuration,

    @Size(min = 2, max = 2)
    Integer[] space

) implements PlantRequestDTO { }
