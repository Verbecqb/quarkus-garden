package org.garden.items.adapter.out.rest_client;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.Duration;
import java.time.Month;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlantSinglePickUpResponseDTO.class, name = "SINGLE"),
        @JsonSubTypes.Type(value = PlantRecurringPickUpResponseDTO.class, name = "RECURRING")
})
@Getter
public abstract class PlantResponseDTO {

    PlantType type;
    
    long id;

    String name;

    Set<Month> plantingSeasons;

    Duration growingDuration;

    @Size(min = 2, max = 2)
    Integer[] space;
}
