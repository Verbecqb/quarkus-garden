package org.garden.planner.infrastructure;

import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
public class SagaFlowDefinitionBuilder<Data> {

    List<Step<Data>> sagaSteps = new LinkedList<>();

    public void addStep(Step<Data> step) {
        sagaSteps.add(step);
    }

    public SagaFlowDefinition<Data> build() {
        return new SagaFlowDefinition<>(this.sagaSteps);
    }

}
