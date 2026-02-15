package com.github.emmanuelAron.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class WebSocketMovePublisher {

    private final ChessWebSocketHandler handler;
    private final ObjectMapper objectMapper;

    public WebSocketMovePublisher(ChessWebSocketHandler handler, ObjectMapper objectMapper) {
        this.handler = handler;
        this.objectMapper = objectMapper;
    }

    public void broadcast(Object payload) {
        try {
            if (payload instanceof String str) {
                handler.broadcast(str);
            } else {
                String json = objectMapper.writeValueAsString(payload);
                handler.broadcast(json);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
    }
}

