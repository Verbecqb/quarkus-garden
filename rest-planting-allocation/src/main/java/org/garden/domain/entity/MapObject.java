package org.garden.domain.entity;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;



@Entity(name = "objects")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name="type",
        discriminatorType = DiscriminatorType.STRING
)
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RaisedBed.class, name = "raised_bed"),
})
public abstract class MapObject extends PanacheEntity {

    private String name;

    @Positive
    private Integer width;

    @Positive
    private Integer length;

    @Size(min = 2, max = 2)
    private Integer[] coordinates;

    @Version
    private int version;


}
