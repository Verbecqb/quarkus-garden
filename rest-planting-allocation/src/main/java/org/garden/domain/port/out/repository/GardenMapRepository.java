package org.garden.domain.port.out.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import org.garden.domain.entity.GardenMapAggregate;

public interface GardenMapRepository extends PanacheRepository<GardenMapAggregate> {
}
