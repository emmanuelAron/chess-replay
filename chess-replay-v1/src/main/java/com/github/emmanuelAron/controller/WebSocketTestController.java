package com.github.emmanuelAron.controller;

import com.github.emmanuelAron.websocket.ChessWebSocketHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketTestController {

    private final ChessWebSocketHandler handler;

    public WebSocketTestController(ChessWebSocketHandler handler) {
        this.handler = handler;
    }

    @GetMapping("/test/chess")
    public String testChessWs() {
        handler.broadcast("e2e4");
        return "Move sent: e2e4";
    }

    // Replay progressif – Espagnole
    @GetMapping("/replayEspagnole")
    public String replayEspagnole() {

        new Thread(() -> {
            try {
                String[] moves = {
                        "e2e4",
                        "e7e5",
                        "g1f3",
                        "b8c6",
                        "f1b5",
                        "a7a6",
                        "b5a4",
                        "g8f6"
                };

                for (String move : moves) {
                    handler.broadcast(move);
                    Thread.sleep(2000); // 2 secondes entre chaque coup
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        return "Replay Espagnole started";
    }
}
