package org.garden.planner.model;


import com.fasterxml.jackson.annotation.JsonTypeName;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import static org.garden.planner.model.enums.Constants.VIRTUAL_FENCE;


@Entity
@DiscriminatorValue(VIRTUAL_FENCE)
@JsonTypeName(VIRTUAL_FENCE)
@Getter
@Setter
public abstract class VirtualFence extends PanacheEntity {


}
