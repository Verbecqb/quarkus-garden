package org.garden.planner;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.garden.clients.ItemCreateRequestDTO;
import org.garden.planner.service.PlannerSagaOrchestratorService;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class PlannerSagaOrchestratorServiceTest {

    @Inject
    PlannerSagaOrchestratorService plannerService;

    @Test
    public void plannerService_success() {

        plannerService.handle(new ItemCreateRequestDTO(1L));

    }

    @Test
    public void plannerService_plant_associated_does_not_exist_fail() {

        plannerService.handle(new ItemCreateRequestDTO(5000L));

    }
}
