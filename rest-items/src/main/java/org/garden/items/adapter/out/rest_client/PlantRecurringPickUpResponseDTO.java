package org.garden.items.adapter.out.rest_client;

import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("RECURRING")
public class PlantRecurringPickUpResponseDTO extends PlantResponseDTO {
        int qty_per_pickup;
}
