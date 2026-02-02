package com.github.emmanuelAron.controller;

import com.github.emmanuelAron.service.OpeningService;
import com.github.emmanuelAron.service.ReplayService;
import com.github.emmanuelAron.websocket.ChessWebSocketHandler;
import jakarta.annotation.PreDestroy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketReplayController {

    private final ReplayService replayService;

    public WebSocketReplayController(ReplayService replayService) {
        this.replayService = replayService;
    }

    @GetMapping("/replay/espagnole/{openingId}/{variationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void replayEspagnole(
            @PathVariable("openingId") String openingId,
            @PathVariable("variationId") String variationId
    ) {
        replayService.replayEspagnole(openingId, variationId);
    }
}

