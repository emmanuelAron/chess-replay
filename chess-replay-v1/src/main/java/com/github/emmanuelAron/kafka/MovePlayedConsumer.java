package com.github.emmanuelAron.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emmanuelAron.events.MovePlayedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class MovePlayedConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(MovePlayedConsumer.class);

    private final ApplicationEventPublisher publisher;
    private final ObjectMapper objectMapper;

    public MovePlayedConsumer(
            ApplicationEventPublisher publisher,
            ObjectMapper objectMapper
    ) {
        this.publisher = publisher;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "chess.events",
            groupId = "chess-replay-group"
    )
    public void onMovePlayed(String payload) {
        try {
            MovePlayedEvent event =
                    objectMapper.readValue(payload, MovePlayedEvent.class);

            log.info("Kafka MOVE_PLAYED received: {}", event);

            // Re-publish as internal Spring domain event
            publisher.publishEvent(event);

        } catch (Exception e) {
            log.error("Failed to deserialize MovePlayedEvent from Kafka", e);
        }
    }
}
