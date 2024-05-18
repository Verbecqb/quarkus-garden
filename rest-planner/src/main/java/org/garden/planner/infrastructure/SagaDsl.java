package org.garden.planner.infrastructure;

public interface SagaDsl<Data> {

    default StepBuilder<Data> step() {
        SagaFlowDefinitionBuilder<Data> builder = new SagaFlowDefinitionBuilder<>();
        return new StepBuilder<>(builder);
    }

}
