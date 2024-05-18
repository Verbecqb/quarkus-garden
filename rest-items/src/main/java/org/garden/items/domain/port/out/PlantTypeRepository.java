package org.garden.items.domain.port.out;

import io.smallrye.mutiny.Uni;
import org.garden.items.adapter.out.rest_client.PlantResponseDTO;

public interface PlantTypeRepository {

    Uni<PlantResponseDTO> get(long id);
}
