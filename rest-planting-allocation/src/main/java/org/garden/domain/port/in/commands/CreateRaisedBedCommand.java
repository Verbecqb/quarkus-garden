package org.garden.domain.port.in.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateRaisedBedCommand (
        @NotNull
        long garden_id,
        @Positive
        Integer width,
        @Positive
        Integer length,
        @Size(min = 2, max = 2)
        Integer[] coordinates,
        @NotBlank
        String name
) implements Command {

        public CreateRaisedBedCommand withGardenId(long garden_id) {
                return new CreateRaisedBedCommand(garden_id, width(), length(), coordinates(), name());
        }
}
