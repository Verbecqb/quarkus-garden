package org.garden.planner.infrastructure;

import io.smallrye.mutiny.Uni;

import java.util.function.Consumer;
import java.util.function.Function;

public class StepBuilder<Data> {

    private final SagaFlowDefinitionBuilder<Data> parent;
    private Function<Data, Uni<Data>>  function;
    private Consumer<Data> compensation;

    public StepBuilder(SagaFlowDefinitionBuilder<Data> builder) {
        this.parent = builder;
    }

    public StepBuilder<Data> invoke(Function<Data, Uni<Data>> function) {
        this.function = function;
        return this;
    }


    public StepBuilder<Data> withCompensation(Consumer<Data> compensation) {
        this.compensation = compensation;
        return this;
    }

    private Step<Data> makeStep(Function<Data, Uni<Data>> function, Consumer<Data> compensation) {
        return new Step<>(function, compensation);
    }

    private Step<Data> makeStep() {
        return new Step<>(this.function, this.compensation);
    }

    public SagaFlowDefinition<Data> build() {
        this.parent.addStep(this.makeStep(function, compensation));
        return this.parent.build();
    }


    public StepBuilder<Data> step() {
        this.parent.addStep(this.makeStep());
        return new StepBuilder<>(this.parent);
    }
}
