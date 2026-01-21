package com.github.emmanuelAron.controller;

import com.github.emmanuelAron.service.OpeningService;
import com.github.emmanuelAron.websocket.ChessWebSocketHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WebSocketTestController {

    private final ChessWebSocketHandler handler;
    private final OpeningService openingService;

    public WebSocketTestController(ChessWebSocketHandler handler,  OpeningService openingService) {
        this.handler = handler;
        this.openingService = openingService;
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

    @GetMapping("/replay/espagnole/{variationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String replayEspagnole(@PathVariable("variationId") String variationId) {

        new Thread(() -> {
            try {
                List<String> moves = openingService.getEspagnoleVariationMoves(variationId);
                for (String move : moves) {
                    handler.broadcast(move);
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        return "Replay Espagnole " + variationId + " started";
    }

}
