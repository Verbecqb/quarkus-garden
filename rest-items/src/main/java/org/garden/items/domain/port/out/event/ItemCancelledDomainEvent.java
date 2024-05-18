package org.garden.items.domain.port.out.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ItemCancelledDomainEvent extends DomainEvent {
    private Long itemId;
}
