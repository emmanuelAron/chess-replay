package com.github.emmanuelAron.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emmanuelAron.events.MovePlayedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka consumer dedicated to analytics processing.
 *
 * This consumer parses domain events from Kafka and prepares them
 * for downstream analytics (database, Spark, etc.).
 */
@Service
@Profile("kafka")
public class AnalyticsKafkaConsumer {

    private final ObjectMapper objectMapper;

    public AnalyticsKafkaConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "chess.events",
            groupId = "analytics"
    )
    public void consume(String message) {
        try {
            // Parse the JSON payload into a domain event
            MovePlayedEvent event =
                    objectMapper.readValue(message, MovePlayedEvent.class);
            // Log useful analytics-oriented information
            System.out.println(
                    "[ANALYTICS] " + event.game().gameId() + ", moveIndex=" + event.move().moveIndex() + " → " + event.move().san()
            );

            // Future steps:
            // - aggregate statistics
            // - persist to Mongo
            // - forward to Spark

        } catch (Exception e) {
            // Never crash the consumer on bad data
            System.err.println("⚠️ [ANALYTICS] Failed to parse event: " + message);
        }
    }
}
