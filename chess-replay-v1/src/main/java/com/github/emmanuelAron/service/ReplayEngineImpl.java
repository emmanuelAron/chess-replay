package com.github.emmanuelAron.service;

import com.github.emmanuelAron.events.EventPublisher;
import com.github.emmanuelAron.events.MovePlayedEvent;
import com.github.emmanuelAron.events.ReplayMode;
import com.github.emmanuelAron.events.ReplaySpeed;
import com.github.emmanuelAron.service.ReplayEngine;
import org.springframework.stereotype.Service;

import java.util.List;

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

        // 2. Replay moves sequentially
        for (MovePlayedEvent move : moves) {
            eventPublisher.publish(move);

            // 3. Control replay speed
            try {
                Thread.sleep(speed.getDelayMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}