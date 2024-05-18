package org.garden.clients;

import jakarta.validation.constraints.NotNull;

public record ItemCreateRequestDTO(

        @NotNull
        Long plantId

) {
}
