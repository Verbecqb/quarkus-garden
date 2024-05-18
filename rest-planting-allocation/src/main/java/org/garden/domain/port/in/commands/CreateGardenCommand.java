package org.garden.domain.port.in.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateGardenCommand(
        @Positive
        Integer width,
        @Positive
        Integer length,
        @NotBlank
        String name
) implements Command {
}
