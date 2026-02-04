package com.github.emmanuelAron.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emmanuelAron.model.Opening;
import com.github.emmanuelAron.model.Variation;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class OpeningService {

    private final ObjectMapper objectMapper;

    public OpeningService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Returns the moves of a given opening variation.
     *
     * @param openingId   opening identifier (ruy_lopez, italian_game, ...)
     * @param variationId variation identifier (CHIGORIN, BREYER, ...)
     */
    public List<String> getVariationMoves(String openingId, String variationId) {

        Opening opening = loadOpening(openingId);

        return opening.variations().stream()
                .filter(v -> v.id().equalsIgnoreCase(variationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown variation '" + variationId + "' for opening '" + openingId + "'"))
                .moves();
    }

    /**
     * Loads an opening definition JSON from the classpath.
     *
     * Expected path:
     *   /openings/{openingId}.json
     */
    private Opening loadOpening(String openingId) {
        String resourcePath = "/openings/" + openingId + ".json";

        try (InputStream is =
                     getClass().getResourceAsStream(resourcePath)) {

            if (is == null) {
                throw new IllegalArgumentException(
                        "Opening file not found: " + resourcePath
                );
            }

            return objectMapper.readValue(is, Opening.class);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load opening file: " + resourcePath, e
            );
        }
    }
}
