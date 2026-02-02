package com.github.emmanuelAron.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emmanuelAron.model.Opening;
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

    public List<String> getEspagnoleVariationMoves(String openingId, String variationId) {

        Opening opening;

        if ("ruy_lopez".equalsIgnoreCase(openingId)) {
            opening = loadRuyLopez();
        } else {
            throw new IllegalArgumentException("Unknown opening: " + openingId);
        }

        return opening.variations().stream()
                .filter(v -> v.id().equalsIgnoreCase(variationId))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Unknown variation: " + variationId))
                .moves();
    }


    private Opening loadRuyLopez() {
        try (InputStream is = getClass()
                .getResourceAsStream("/openings/ruy_lopez.json")) {

            if (is == null) {
                throw new IllegalStateException("ruy_lopez.json not found");
            }

            return objectMapper.readValue(is, Opening.class);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load ruy_lopez.json", e);
        }
    }
}
