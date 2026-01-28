package com.github.emmanuelAron.kafka;

import com.github.emmanuelAron.websocket.ChessWebSocketHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Kafka consumer responsible for replaying chess moves.
 *
 * Consumes MovePlayedEvent messages from Kafka and forwards them
 * to connected WebSocket clients for UI replay.
 */
@Service
@Profile("kafka")
public class ReplayKafkaConsumer {

    /**
     * WebSocket handler used to broadcast events to frontend clients.
     */
    private final ChessWebSocketHandler webSocketHandler;

    /**
     * Constructor-based dependency injection.
     *
     * @param webSocketHandler component responsible for WebSocket communication
     */
    public ReplayKafkaConsumer(ChessWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    /**
     * Kafka listener method.
     *
     * This method is invoked automatically whenever a new message
     * is published to the Kafka topic.
     *
     * Messages are consumed in the order guaranteed by Kafka
     * within a partition.
     *
     * @param message JSON representation of a domain event
     */
    @KafkaListener(
            topics = "chess.events",
            groupId = "websocket-replay"
    )
    public void consume(String message) {
        System.out.println("📥 [REPLAY-CONSUMER] Received event → " + message);
        // Forward the Kafka event directly to all connected WebSocket clients
        // without modifying or interpreting the payload
        webSocketHandler.broadcast(message);
    }
}
