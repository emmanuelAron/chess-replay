package com.github.emmanuelAron.controller;

import com.github.emmanuelAron.service.OpeningService;
import com.github.emmanuelAron.service.ReplayService;
import com.github.emmanuelAron.websocket.ChessWebSocketHandler;
import jakarta.annotation.PreDestroy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/replay")
public class WebSocketReplayController {

    private final ReplayService replayService;

    public WebSocketReplayController(ReplayService replayService) {
        this.replayService = replayService;
    }

//    @GetMapping("/replay/espagnole/{openingId}/{variationId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void replayEspagnole(@PathVariable("openingId") String openingId, @PathVariable("variationId") String variationId) {
//        replayService.replayEspagnole(openingId, variationId);
//    }

    @GetMapping("/{opening}/{openingId}/{variationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void replay(@PathVariable("opening") String opening, @PathVariable("openingId") String openingId, @PathVariable("variationId") String variationId) {
        replayService.replay(opening, openingId, variationId);
    }
}

