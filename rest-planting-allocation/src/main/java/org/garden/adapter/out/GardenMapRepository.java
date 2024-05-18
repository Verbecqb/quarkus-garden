package org.garden.adapter.out;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.garden.domain.entity.GardenMapAggregate;

import java.time.LocalDate;


@ApplicationScoped
class GardenMapRepository implements org.garden.domain.port.out.repository.GardenMapRepository {

    //TODO implement it
    public Uni<GardenMapAggregate> loadPartialAggregate(long id, LocalDate startDate, LocalDate endDate) {
        return this.find("select g from GardenMapAggregate g " +
                "LEFT JOIN FETCH g.mapObjects m " +
                "WHERE TYPE(m) = RaisedBed").firstResult();
    }


}
