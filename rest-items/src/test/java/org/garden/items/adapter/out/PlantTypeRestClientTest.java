package org.garden.items.adapter.out;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.garden.items.Utils;
import org.garden.items.configuration.InjectWiremock;
import org.garden.items.configuration.PlantTypeWiremock;
import org.garden.items.domain.port.out.PlantTypeRepository;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;

@QuarkusTest
@QuarkusTestResource(PlantTypeWiremock.class)
public class PlantTypeRestClientTest {

    private static final String PLANT_URL = "/v0/plant_types";

    @Inject
    PlantTypeRepository plantTypeClient;

    @InjectWiremock
    WireMockServer wireMockServer;

    @BeforeEach
    public void beforeEach() {
        this.wireMockServer.resetAll();
    }

    @Inject Utils utils;

    @Test
    void getPlantType() {

        utils.getDefaultPlantJSON();

        this.wireMockServer.stubFor(
                get(urlEqualTo(PLANT_URL + "/1"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(utils.getDefaultPlantJSON())
                        )
        );

        var response = this.plantTypeClient.get(1L).log()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertSubscribed()
                .awaitItem(Duration.ofSeconds(10))
                .getItem();


        assertNotNull(response);
        assertEquals(response.getId(), 5L);


    }

    @Test
    public void recoverFrom404() {

        wireMockServer.stubFor(
                get(urlEqualTo(PLANT_URL + "/5"))
                        .willReturn(notFound())
        );

        // Assert the PlantTypeClient returns null in case of 404 response
        this.plantTypeClient.get(5).subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertSubscribed()
                .awaitItem()
                .assertItem(null);

        // HTTP call is done only ONCE (no @retry)
        this.wireMockServer.verify(1,
                getRequestedFor(urlEqualTo(PLANT_URL + "/5"))
        );

    }

    @Test
    public void retryOnHTTPServerError() {

        this.wireMockServer.stubFor(
              get(anyUrl()).willReturn(serverError())
        );

        this.plantTypeClient.get(1L).subscribe()
                .withSubscriber(UniAssertSubscriber.create())
                .assertSubscribed()
                .awaitFailure(Duration.ofSeconds(30))
                .assertFailedWith(ClientWebApplicationException.class);

        this.wireMockServer.verify(1 + 3,
                getRequestedFor(anyUrl())
        );

    }

    @Test
    public void retryAfterTimeout() {

        this.wireMockServer.stubFor(
                get(anyUrl()).willReturn(aResponse().withFixedDelay(6000))
        );

        this.plantTypeClient.get(1L)
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create())
                .assertSubscribed()
                .awaitFailure(Duration.ofSeconds(30)) // await for duration greater than 3 * timeout
                .assertFailedWith(TimeoutException.class);

        // HTTP call is done 1 (initial call) + 3 times (see @Retry configuration )
        this.wireMockServer.verify(1 + 3,
                getRequestedFor(urlEqualTo(PLANT_URL + "/1"))
        );

    }
}
