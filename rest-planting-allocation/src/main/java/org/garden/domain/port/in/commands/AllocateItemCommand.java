package org.garden.domain.port.in.commands;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AllocateItemCommand (
        long item_id,
        @NotNull
        int space_width,
        @NotNull
        int space_length,
        LocalDate from,
        LocalDate to
) implements Command { }
