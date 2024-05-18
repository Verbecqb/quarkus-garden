package org.garden.items.framework;

import io.smallrye.mutiny.Uni;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.garden.items.domain.entity.ItemAggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@NoArgsConstructor
@AllArgsConstructor
public class CommandHandlers {

    private List<CommandHandler> handlers = new ArrayList<>();

    public <C extends Command> Optional<CommandHandler> findTargetMethod(C command) {
        return handlers.stream().filter(e -> e.handles(command)).findFirst();
    }

    public CommandHandlersBuilder getBuilder() {
        return new CommandHandlersBuilder();
    }


    public static class CommandHandlersBuilder{

        List<CommandHandler> handlers = new ArrayList<>();

        public <C extends Command> CommandHandlersBuilder onMessage(
                Class<C> commandClass,
                Function<C, Uni<ItemAggregate>> handler) {

            handlers.add(new CommandHandler(handler, commandClass));
            return this;
        }

        public CommandHandlers build() {
            return new CommandHandlers(handlers);
        }
    }


}


