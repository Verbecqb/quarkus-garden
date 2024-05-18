package org.garden.items.domain.port.in.commands;

import jakarta.validation.constraints.NotNull;
import org.garden.items.framework.Command;

public record ConfirmCreateItemCommand(
        @NotNull
        Long itemId
) implements Command { }
