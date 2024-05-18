package org.garden.items.adapter.out.persistence;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.garden.items.domain.entity.ItemAggregate;

@ApplicationScoped
public class ItemRepository implements PanacheRepository<ItemAggregate> {

}
