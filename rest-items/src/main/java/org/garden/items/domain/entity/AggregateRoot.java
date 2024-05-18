package org.garden.items.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class AggregateRoot {

    protected long aggregate_id;
    protected String aggregate_type;

}
