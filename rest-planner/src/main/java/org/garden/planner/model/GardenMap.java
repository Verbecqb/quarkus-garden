package org.garden.planner.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;

import java.util.Map;

@Entity(name = "garden_map")
@Getter
public class GardenMap extends PanacheEntity {

    @Transient
    private Map<MapObject, int[]> raisedBedsByLocation;

    public void addObjectToMap(MapObject object, int[] position) {

        // TODO check if the position is available

        raisedBedsByLocation.put(object, position);
    }
}
