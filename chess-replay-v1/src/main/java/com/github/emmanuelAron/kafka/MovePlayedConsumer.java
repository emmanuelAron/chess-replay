package com.github.emmanuelAron.kafka;

import com.github.emmanuelAron.events.MovePlayedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MovePlayedConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    public MovePlayedConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(
            topics = "chess.events",
            groupId = "chess-replay-group"
    )
    public void onMovePlayed(MovePlayedEvent event) {
        System.out.println("♻️ Kafka event received: " + event);

        messagingTemplate.convertAndSend(
                "/topic/game",
                event
        );
    }
}
