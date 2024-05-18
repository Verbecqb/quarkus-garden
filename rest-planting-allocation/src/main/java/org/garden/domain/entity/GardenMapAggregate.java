package org.garden.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.garden.domain.events.schema.AllocationEvent;
import org.garden.domain.events.schema.MapObjectCreatedEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDate;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
public class GardenMapAggregate extends AggregateRoot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    int width;
    @NotNull
    int length;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<MapObject> mapObjects = new HashSet<>();

    // private constructor TODO MapStruct requires a constructor, to be investigated.

    public MapObjectCreatedEvent addMapObject(MapObject m) {

        // Invariant --> 1. Map object should fit in the Garden and not overlay existing ones
        if (m.getLength() > this.length || m.getWidth() > this.width) {
            //TODO return Event with failure
        }
        // Invariant --> 2. Map object should not overlay other map objects

        this.mapObjects.add(m);

        return new org.garden.domain.events.schema.MapObjectCreatedEvent(
                this.id,
                ((RaisedBed) m).getName(),
                m.getWidth(), m.getLength(),
                Arrays.stream(m.getCoordinates()).toList()
        );
    }

    /**
     * @param item_id
     * @param space_width
     * @param space_length
     * @param from
     * @param to
     * @return
     */
    public AllocationEvent allocateItem(long item_id,
                              int space_width,
                              int space_length,
                              LocalDate from,
                              LocalDate to) {

        try {
            Optional<Pair<RaisedBed, Optional<Integer[]>>> res = this.getMapObjects().stream()
                    // Only take Object for growing
                    .filter(obj -> obj instanceof RaisedBed)
                    // Filter out the raised beds without space available for required w/l and duration
                    .filter((raised_bed) ->
                            ((RaisedBed) raised_bed).findAvailableSpace(
                                    space_width,
                                    space_length,
                                    from,
                                    to).isPresent()
                    )
                    .findFirst()
                    .map(b ->
                            Pair.of(
                                    (RaisedBed) b,
                                    ((RaisedBed) b).findAvailableSpace(
                                            space_width,
                                            space_length,
                                            from,
                                            to
                                    )
                            )
                    );

            if (res.isPresent()) {

                // On the RaisedBed with a space available, add schedule and generate allocated event.
                res.get().getKey().addSchedule(
                        item_id,
                        res.get().getRight().get()[0],
                        res.get().getRight().get()[1],
                        space_width, space_length,
                        from, to
                );

                return new org.garden.domain.events.schema.AllocationEvent(item_id, org.garden.domain.events.schema.AllocationStatus.RESERVED);

            } else {
                return new org.garden.domain.events.schema.AllocationEvent(item_id, org.garden.domain.events.schema.AllocationStatus.REJECTED);
                    
            }


        } catch (IllegalArgumentException | NoSuchElementException ex ) {
                return new org.garden.domain.events.schema.AllocationEvent(item_id, org.garden.domain.events.schema.AllocationStatus.REJECTED);
        }

    }


}
