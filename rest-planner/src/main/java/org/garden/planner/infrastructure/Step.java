package org.garden.planner.infrastructure;

import io.smallrye.mutiny.Uni;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Consumer;
import java.util.function.Function;

@AllArgsConstructor
@Getter
public class Step<Data> {

    private Function<Data, Uni<Data>> function;
    private Consumer<Data> compensation;

}
