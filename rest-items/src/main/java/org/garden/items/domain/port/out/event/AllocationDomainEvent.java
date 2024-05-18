package org.garden.items.domain.port.out.event;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import org.garden.items.domain.constants.AllocationEventStatus;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AllocationDomainEvent extends DomainEvent {
    
    long item_id;

    @NotNull
    AllocationEventStatus allocationEventStatus;
}
