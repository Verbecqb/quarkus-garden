package org.garden.planner.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.garden.clients.ItemCreateRequestDTO;
import org.garden.planner.clients.ItemClientProxy;
import org.garden.planner.infrastructure.SagaDsl;
import org.garden.planner.infrastructure.SagaFlowDefinition;
import org.garden.planner.service.steps.WorkflowStepStatus;

@ApplicationScoped
public class PlannerSagaOrchestratorService implements SagaDsl<CreateItemSagaData> {

    @Inject
    @RestClient
    ItemClientProxy itemClientProxy;


    private final SagaFlowDefinition<CreateItemSagaData> sagaDefinition =
            step()
                    .invoke(this::createItem)
                    .withCompensation(this::rejectItem)
            .build();

    public void handle(ItemCreateRequestDTO itemCreateRequestDTO) {
        CreateItemSagaData createItemSagaData = new CreateItemSagaData(itemCreateRequestDTO);
        this.start(createItemSagaData);
    }

    private void start(CreateItemSagaData createItemSagaData) {
        sagaDefinition.start(createItemSagaData);
    }

    private Uni<CreateItemSagaData> createItem(@NotNull CreateItemSagaData createItemSagaData) {
        System.out.println("\t createItem called");
        return itemClientProxy.create_seed_item(createItemSagaData.getItemDTO())
                .invoke((e) -> {
                    if ((e.hasEntity())) {
                        createItemSagaData.setItemCreateStepStatus(WorkflowStepStatus.SUCCESS);
                    } else {
                        createItemSagaData.setItemCreateStepStatus(WorkflowStepStatus.FAILURE);
                    }
                }).replaceWith(createItemSagaData);
    }

    private void rejectItem(CreateItemSagaData createItemSagaData) {

        System.out.println("\t rejected item");

    }


}
