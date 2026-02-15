package com.github.emmanuelAron.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class ChessWebSocketHandler extends TextWebSocketHandler {

    private static final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("WebSocket connected: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session);
        System.out.println("WebSocket closed: " + session.getId());
    }

    public void broadcast(String message) {
        System.out.println("Broadcasting to WebSocket: " + message);
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
                }else {
                    System.out.println("WS CLOSED (skip send): " + session.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
