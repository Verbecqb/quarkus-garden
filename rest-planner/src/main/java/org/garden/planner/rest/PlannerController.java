package org.garden.planner.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.garden.clients.ItemCreateRequestDTO;
import org.garden.planner.service.PlannerSagaOrchestratorService;

@Path("/v0/planner")
public class PlannerController {

    @Inject
    PlannerSagaOrchestratorService plannerSagaOrchestratorService;

    @POST
    public void createItem(ItemCreateRequestDTO itemCreateRequestDTO) {
        plannerSagaOrchestratorService.handle(itemCreateRequestDTO);

    }

}
