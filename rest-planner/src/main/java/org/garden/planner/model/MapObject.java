package org.garden.planner.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import static org.garden.planner.model.enums.Constants.VIRTUAL_FENCE;


@Entity(name = "objects")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name="type",
        discriminatorType = DiscriminatorType.STRING
)
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = VirtualFence.class, name = VIRTUAL_FENCE)
})
public abstract class MapObject extends PanacheEntity {

    @Positive
    private Integer width;

    @Positive
    private Integer length;

    @Size(min = 2, max = 2)
    private Integer[] coordinates;

    @Version
    private int version;

}
