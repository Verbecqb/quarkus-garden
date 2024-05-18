package org.garden.planner.service;

import lombok.Data;
import org.garden.clients.ItemCreateRequestDTO;
import org.garden.planner.service.steps.WorkflowStepStatus;

@Data
public class CreateItemSagaData {

    private long id;
    private ItemCreateRequestDTO itemDTO;
    private WorkflowStepStatus itemCreateStepStatus;
    private WorkflowStepStatus allocationRaisedBedStatus;

    public CreateItemSagaData(ItemCreateRequestDTO dto) {
        this.itemDTO = dto;
    }
}
