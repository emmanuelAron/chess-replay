package com.github.emmanuelAron.config;

import com.github.emmanuelAron.websocket.ChessWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChessWebSocketHandler handler;

    public WebSocketConfig(ChessWebSocketHandler handler) {
        System.out.println("WebSocketConfig loaded");
        this.handler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/chess")
                .setAllowedOriginPatterns("*"); // autoriser React localhost:3000

    }
}
