package org.garden.items.configuration;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;

import java.util.HashMap;
import java.util.Map;

public class KafkaTestResourceLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private final String CHANNEL_IN = "SAGA_ITEM_ALLOCATION_EVENT";
    private final String CHANNEL_OUT = "ITEMS_DOMAIN_EVENTS";

    @Override
    public Map<String, String> start() {
        Map<String, String> env = new HashMap<>();

        Map<String, String> props1 = InMemoryConnector.switchIncomingChannelsToInMemory(CHANNEL_IN);
        Map<String, String> props2 = InMemoryConnector.switchOutgoingChannelsToInMemory(CHANNEL_OUT);

        env.putAll(props1);
        env.putAll(props2);

        return env;
    }

    @Override
    public void stop() {
        InMemoryConnector.clear();
    }
}
