package org.garden.domain.entity;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.garden.constants.PlantType;

import java.time.Duration;
import java.time.Month;
import java.util.Set;

@Entity(name = "RECURRING_PICKUP_PLANTS")
@Getter
@NoArgsConstructor
public class PlantRecurringPickup extends PlantEntity {
    private int qty_per_pickup;

    // TODO Manual generation - need to find a cleaner approach
    //  SuperBuilder could be used but it requires ALL supers to also be annotated w/ @SuperBuilder - which is not the case fo a Panache Entity
    // https://projectlombok.org/features/experimental/SuperBuilder
    @Builder
    public PlantRecurringPickup(int qty_per_pickup, PlantType type, Long id, String name, Set<Month> plantingSeasons, Duration growingDuration, Integer[] space, Integer version) {
        super(type, id, name, plantingSeasons, growingDuration, space, version);
        this.qty_per_pickup = qty_per_pickup;
    }
}
