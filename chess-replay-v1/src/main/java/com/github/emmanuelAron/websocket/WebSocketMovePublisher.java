package com.github.emmanuelAron.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emmanuelAron.events.MovePlayedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WebSocketMovePublisher {

    private final ChessWebSocketHandler handler;
    private final ObjectMapper objectMapper;

    public WebSocketMovePublisher(
            ChessWebSocketHandler handler,
            ObjectMapper objectMapper
    ) {
        this.handler = handler;
        this.objectMapper = objectMapper;
    }

    @EventListener
    public void onMovePlayed(MovePlayedEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            handler.broadcast(json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize MovePlayedEvent", e);
        }
    }
}
