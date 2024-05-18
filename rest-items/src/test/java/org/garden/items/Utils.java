package org.garden.items;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.garden.items.adapter.out.rest_client.PlantResponseDTO;
import org.garden.items.adapter.out.rest_client.PlantSinglePickUpResponseDTO;
import org.garden.items.adapter.out.rest_client.PlantType;

import java.lang.reflect.Field;
import java.time.Duration;

@QuarkusTest
public class Utils {

    @Inject
    ObjectMapper objectMapper;

    public static PlantResponseDTO getDefaultPlantResponseDTO() {
        PlantSinglePickUpResponseDTO response = new PlantSinglePickUpResponseDTO();

        // PlantResponseDTO has no Setter. Use reflection to build the object
        try {

            Field idField = response.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(response, 5);

            Field typeField = response.getClass().getSuperclass().getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(response, PlantType.SINGLE);

            Field durationField = response.getClass().getSuperclass().getDeclaredField("growingDuration");
            durationField.setAccessible(true);
            durationField.set(response, Duration.ofDays(5));

            Field spaceField = response.getClass().getSuperclass().getDeclaredField("space");
            spaceField.setAccessible(true);
            spaceField.set(response, new Integer[]{5, 2});

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    public String getDefaultPlantJSON() {
        try {
           return objectMapper.writeValueAsString(getDefaultPlantResponseDTO());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
