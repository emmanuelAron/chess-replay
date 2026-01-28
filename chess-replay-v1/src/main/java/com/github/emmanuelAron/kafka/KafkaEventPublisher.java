package com.github.emmanuelAron.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emmanuelAron.events.EventPublisher;
import com.github.emmanuelAron.events.MovePlayedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka-based implementation of the EventPublisher.
 *
 * This publisher is responsible for sending domain events
 * to a Kafka topic in JSON format.
 *
 * It is only active when the "kafka" Spring profile is enabled.
 */
@Profile("kafka")
@Service
public class KafkaEventPublisher implements EventPublisher {

    /**
     * Spring Kafka template used to send messages to Kafka.
     * <String, String> means:
     *  - key is a String
     *  - value (payload) is a JSON String
     */
    private final KafkaTemplate<String, String> kafkaTemplate;
    /**
     * Jackson ObjectMapper used to serialize Java objects
     * (domain events) into JSON strings.
     */
    private final ObjectMapper objectMapper;
    /**
     * Kafka topic used to publish all chess domain events.
     *
     * This topic contains different event types
     * (e.g. MOVE_PLAYED, REPLAY_STARTED, REPLAY_FINISHED).
     */
    private static final String TOPIC = "chess.events";

    /**
     * Constructor-based dependency injection.
     *
     * @param kafkaTemplate Kafka producer abstraction provided by Spring
     * @param objectMapper Jackson JSON serializer
     */
    public KafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate,
                               ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Publishes a domain event to Kafka.
     *
     * The event object is serialized to JSON and sent to the Kafka topic.
     * Kafka acts as an event backbone; it does not interpret the payload.
     *
     * @param event domain event to publish
     */
    @Override
    public void publish(Object event) {
        try {
            // Serialize the event object into a JSON string
            String payload = objectMapper.writeValueAsString(event);

            // Extract Kafka key from the domain event (one partition per game)
            String key = null;
            if (event instanceof MovePlayedEvent movePlayedEvent) {
                key = movePlayedEvent.game().gameId();
            }

            // Log the event before publishing (useful for debugging and demos)
            System.out.println("[KAFKA-PUBLISH] Sending event to topic " + TOPIC + " → " + payload);

            // Send the JSON payload to the Kafka topic
            kafkaTemplate.send(TOPIC, payload);
        } catch (Exception e) {
            // Wrap any serialization or Kafka error into a runtime exception
            throw new RuntimeException("Failed to publish event to Kafka", e);
        }
    }
}
