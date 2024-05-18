package org.garden.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.garden.domain.entity.RaisedBed;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RaisedBed.class, name = "raised_bed")
})
public record MapObjectDTO(
        @Positive
        Integer width,
        @Positive
        Integer length,
        @Size(min = 2, max = 2)
        Integer[] coordinates,
        String name
) {
}
