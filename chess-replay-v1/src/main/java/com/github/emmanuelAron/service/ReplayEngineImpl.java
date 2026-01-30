package com.github.emmanuelAron.service;

import com.github.emmanuelAron.events.*;
import com.github.emmanuelAron.service.ReplayEngine;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

// service/ReplayEngineImpl.java
@Service
public class ReplayEngineImpl implements ReplayEngine {

    private final EventPublisher eventPublisher;
    private final GameLoader gameLoader;

    public ReplayEngineImpl(EventPublisher eventPublisher, GameLoader gameLoader) {
        this.eventPublisher = eventPublisher;
        this.gameLoader = gameLoader;
    }

    @Override
    public void replayGame(String gameId, ReplayMode mode, ReplaySpeed speed) {
        // orchestration
        // 1. Load all moves for the game
        List<MovePlayedEvent> moves = gameLoader.loadGame(gameId);

        for (MovePlayedEvent event : moves) {
            eventPublisher.publish(event);

            try {
                Thread.sleep(speed.getDelayMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}