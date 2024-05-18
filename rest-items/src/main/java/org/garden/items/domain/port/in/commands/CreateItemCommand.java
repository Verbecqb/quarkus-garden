package org.garden.items.domain.port.in.commands;

import org.garden.items.framework.Command;

public record CreateItemCommand(
        long plantTypeId
) implements Command {
}
