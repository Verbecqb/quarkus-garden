package org.garden.domain.entity;


import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.garden.constants.PlantType;
import org.hibernate.annotations.NaturalId;

import java.time.Duration;
import java.time.Month;
import java.util.Set;

@Entity(name = "PLANTS")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class PlantEntity extends PanacheEntityBase {

    @NotNull
    private PlantType type;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NaturalId
    private String name;

    @Size(min = 1)
    @NotNull
    private Set<Month> plantingSeasons;

    @NotNull
    private Duration growingDuration;

    @Size(min = 2, max = 2)
    private Integer[] space;

    @Version
    private Integer version;

}
