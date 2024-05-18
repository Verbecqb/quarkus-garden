package org.garden.items.domain.port.in;

import io.smallrye.mutiny.Uni;
import org.garden.items.domain.entity.ItemAggregate;
import org.garden.items.framework.Command;

public interface CommandHandlerInterface {

    <C extends Command> Uni<ItemAggregate> handleMessage(Command command) ;
}
