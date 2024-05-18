package org.garden.items.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;
import org.apache.commons.lang3.tuple.Pair;
import org.garden.items.domain.constants.PlantState;
import org.garden.items.domain.port.out.event.DomainEvent;
import org.garden.items.domain.port.out.event.ItemCancelledDomainEvent;
import org.garden.items.domain.port.out.event.ItemCreatedDomainEvent;


import java.time.LocalDate;
import java.util.Optional;

@Entity(name = "ITEM")
@AllArgsConstructor
@Setter
@Getter
public class ItemAggregate  {

     // Private constructor
     private ItemAggregate() { }

     public ItemAggregate(Long plantTypeId) {
          this.state = PlantState.AWAITING_ALLOCATION;
          this.plantTypeId = plantTypeId;
          this.created_date = LocalDate.now();
     }

     public static Pair<ItemAggregate, ItemCreatedDomainEvent> create(Long plantTypeId) {
          ItemAggregate itemAggregate = new ItemAggregate(plantTypeId);
          return Pair.of(
                  itemAggregate,
                  ItemCreatedDomainEvent.builder().build()
          );
     }

     public Optional<DomainEvent> takeAction() {
          return state.nextStep(this);
     }

     public DomainEvent cancelItem() {
          this.setState(PlantState.FAILURE);
          return ItemCancelledDomainEvent.builder().itemId(this.getId()).build();
     }

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;

     private Long plantTypeId;

     @Enumerated(EnumType.STRING)
     private PlantState state;

     @FutureOrPresent
     private LocalDate created_date;

     @FutureOrPresent
     private LocalDate planted_date;

     @FutureOrPresent
     private LocalDate pickup_date;

     @Version
     private int version;


}
