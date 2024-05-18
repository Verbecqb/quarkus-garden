package org.garden.items.adapter.out.rest_client;

import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.temporal.ChronoUnit;


/**
 * Bean to be used for interacting with the PlantType REST service
 */
@ApplicationScoped
class PlantTypeClient implements PlantTypeProxy {

    @Inject
    @RestClient PlantTypeProxy plantTypeRESTClient;

    @Retry(maxRetries = 3, delay = 200, delayUnit = ChronoUnit.MILLIS)
    @Timeout(value = 4, unit = ChronoUnit.SECONDS)
    public Uni<PlantResponseDTO> get(long id) {

        return this.plantTypeRESTClient.get(id)
                .onFailure(Is404Exception.IS_404).recoverWithNull();
    }

}
