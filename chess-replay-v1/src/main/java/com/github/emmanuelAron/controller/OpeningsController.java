package com.github.emmanuelAron.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * REST controller responsible for serving chess openings data.
 *
 * This controller exposes:
 *  - a global openings index (navigation structure)
 *  - individual opening definition files (moves, variations, etc.)
 *
 * All data is stored as static JSON files in:
 *   src/main/resources/openings/
 */
@RestController
@RequestMapping("/api/openings")
public class OpeningsController {

    /**
     * Returns the global openings index.
     *
     * This file is used ONLY for navigation in the frontend
     * (categories, openings list, file references).
     *
     * Example:
     *   GET /api/openings/index
     */
    @GetMapping(value = "/index", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOpeningsIndex() throws IOException {
        // Load the index JSON file from the classpath
        ClassPathResource resource = new ClassPathResource("openings/openings_index.json");
        // Read the entire file content as a UTF-8 string
        String json = StreamUtils.copyToString(
                resource.getInputStream(),
                StandardCharsets.UTF_8
        );
        // Return raw JSON content to the client
        return ResponseEntity.ok(json);
    }
    /**
     * Returns a specific opening definition file.
     *
     * Each opening is stored in its own JSON file
     * (e.g. italian_game.json, ruy_lopez.json).
     *
     * Example:
     *   GET /api/openings/ruy_lopez.json
     */
    @GetMapping(value = "/{file:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOpeningFile(
            @PathVariable("file") String file) throws IOException {
        // Build a classpath reference to the requested opening file
        ClassPathResource resource = new ClassPathResource("openings/" + file);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        // Read the JSON file content
        String json = StreamUtils.copyToString(
                resource.getInputStream(),
                StandardCharsets.UTF_8
        );

        return ResponseEntity.ok(json);
    }
}
