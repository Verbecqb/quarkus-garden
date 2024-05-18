package org.garden.domain.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "OCCUPANCY_RAISED_BED")
@Table
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRaisedBedItems extends PanacheEntity {

    long item_id;

    @NotNull
    LocalDate day;

    int coordinateX;
    int coordinateY;



}
