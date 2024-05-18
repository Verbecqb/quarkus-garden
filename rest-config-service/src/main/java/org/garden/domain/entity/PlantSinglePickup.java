package org.garden.domain.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.garden.constants.PlantType;

import java.time.Duration;
import java.time.Month;
import java.util.Set;

@Entity(name = "SINGLE_PICKUP_PLANTS")
@Getter
@NoArgsConstructor
public class PlantSinglePickup extends PlantEntity {

    // TODO Manual generation - need to find a cleaner approach
    //  SuperBuilder could be used but it requires ALL supers to also be annotated w/ @SuperBuilder - which is not the case fo a Panache Entity
    // https://projectlombok.org/features/experimental/SuperBuilder
    @Builder
    public PlantSinglePickup(PlantType type, Long id, String name, Set<Month> plantingSeasons, Duration growingDuration, @Size(min = 2, max = 2) Integer[] space, Integer version) {
        super(type, id, name, plantingSeasons, growingDuration, space, version);
    }
}
