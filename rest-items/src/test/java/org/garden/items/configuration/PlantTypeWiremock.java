package org.garden.items.configuration;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


public class PlantTypeWiremock implements QuarkusTestResourceLifecycleManager {

    private final WireMockServer wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());


    @Override
    public Map<String, String> start() {

        wireMockServer.start();
        var url = "localhost:%d".formatted(
                this.wireMockServer.isHttpsEnabled() ? this.wireMockServer.httpsPort() : this.wireMockServer.port()
        );
        return Collections.singletonMap("quarkus.rest-client.plant-types-rest-client.url", wireMockServer.baseUrl());
    }

    @Override
    public void stop() {
            wireMockServer.stop();
    }

    @Override
    public void inject(TestInjector testInjector) {
        testInjector.injectIntoFields(
                this.wireMockServer,
                new TestInjector.AnnotatedAndMatchesType(InjectWiremock.class, WireMockServer.class)
        );
    }
}
