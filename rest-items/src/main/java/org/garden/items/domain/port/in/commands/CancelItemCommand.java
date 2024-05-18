package org.garden.items.domain.port.in.commands;


import org.garden.items.framework.Command;

public record CancelItemCommand(
        Long itemId
) implements Command  { }
