package com.github.emmanuelAron.controller;

import com.github.emmanuelAron.events.ReplayMode;
import com.github.emmanuelAron.events.ReplaySpeed;
import com.github.emmanuelAron.service.ReplayEngine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ReplayTestController {

    private final ReplayEngine replayEngine;

    public ReplayTestController(ReplayEngine replayEngine) {
        this.replayEngine = replayEngine;
    }

    @GetMapping("/kafka")
    public void testKafka() {
        replayEngine.replayGame("game-test", ReplayMode.EVENT_DRIVEN, ReplaySpeed.NORMAL);
    }
}
