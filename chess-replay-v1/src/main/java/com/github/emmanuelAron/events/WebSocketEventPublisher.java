package com.github.emmanuelAron.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emmanuelAron.websocket.ChessWebSocketHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * WebSocket-based implementation of EventPublisher.
 *
 * This publisher broadcasts replay events directly to
 * connected WebSocket clients in real time.
 *
 * Used in "Simple Replay" mode or as a delivery channel
 * for event-driven replays.
 */
@Profile("default")
@Service
public class WebSocketEventPublisher implements EventPublisher {

    private final ChessWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    public WebSocketEventPublisher(ChessWebSocketHandler webSocketHandler, ObjectMapper objectMapper) {
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(Object event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            webSocketHandler.broadcast(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}
