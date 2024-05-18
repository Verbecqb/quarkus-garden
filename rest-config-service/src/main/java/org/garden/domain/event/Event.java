package org.garden.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Event {
    public static final String ITEM_SEED_CREATED = "ItemSeedCreated";

    private UUID uuid;
    private String eventType;
    private LocalDate timestamp;

}
