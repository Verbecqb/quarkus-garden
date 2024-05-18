package org.garden.planner.infrastructure;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SagaFlowDefinition<Data> {

    private List<Step<Data>> steps;

    public void start(Data data) {
        for(Step<Data> step : steps) {
            step.getFunction().apply(data);
        }
    }

}
