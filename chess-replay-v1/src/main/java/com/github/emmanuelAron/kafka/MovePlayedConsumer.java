package com.github.emmanuelAron.kafka;

import com.github.emmanuelAron.events.MovePlayedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class MovePlayedConsumer {

    private final ApplicationEventPublisher publisher;

    public MovePlayedConsumer(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @KafkaListener(
            topics = "chess.events",
            groupId = "chess-replay-group"
    )
    public void onMovePlayed(MovePlayedEvent event) {
        System.out.println("Kafka event received: " + event);

        // Publish internal Spring event
        publisher.publishEvent(event);
    }
}
