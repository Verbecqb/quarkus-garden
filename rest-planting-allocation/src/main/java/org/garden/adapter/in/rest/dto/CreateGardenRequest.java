package org.garden.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateGardenRequest(
        @Positive
        Integer width,
        @Positive
        Integer length,
        @NotBlank
        String name
) {
}
