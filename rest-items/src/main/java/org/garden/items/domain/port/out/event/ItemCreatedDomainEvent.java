package org.garden.items.domain.port.out.event;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ItemCreatedDomainEvent extends DomainEvent {

    private Long itemId;
    private Integer[] spaceRequired;
    private LocalDate from;
    private LocalDate to;

}
