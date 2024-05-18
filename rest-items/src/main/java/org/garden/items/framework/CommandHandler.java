package org.garden.items.framework;

import io.smallrye.mutiny.Uni;
import org.garden.items.domain.entity.ItemAggregate;

import java.util.function.Function;


public class CommandHandler {


    private final Function<Command, Uni<ItemAggregate>> handler;
    private final Class<?> commandClass;


    public <C extends Command> CommandHandler(Function<C, Uni<ItemAggregate>> handler, Class<?> commandClass) {
        this.handler = (Function<Command, Uni<ItemAggregate>>) handler;
        this.commandClass = commandClass;
    }

    public <C extends Command> boolean handles(C command) {
        return command.getClass().isAssignableFrom(commandClass);
    }

    public <C extends Command> Uni<ItemAggregate> invokeMethod(C c) {
        return handler.apply(c);
    }
}
