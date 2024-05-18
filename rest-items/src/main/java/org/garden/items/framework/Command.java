package org.garden.items.framework;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.garden.items.domain.port.in.commands.CancelItemCommand;
import org.garden.items.domain.port.in.commands.ConfirmCreateItemCommand;
import org.garden.items.domain.port.in.commands.CreateItemCommand;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "allocationStatus", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CancelItemCommand.class, name = "REJECTED"),
        @JsonSubTypes.Type(value = CreateItemCommand.class, name = "create_item"),
        @JsonSubTypes.Type(value = ConfirmCreateItemCommand.class, name = "RESERVED"),
})
public interface Command {

}

