package com.github.emmanuelAron.service;


import com.github.emmanuelAron.websocket.ChessWebSocketHandler;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Replays a chess variation by broadcasting structured MOVE events
 * to connected WebSocket clients at a fixed interval.
 *
 * Each move is wrapped in a JSON message containing a "type" field
 * to support frontend event differentiation (MOVE vs STAT).
 */
@Service
public class ReplayService {

    private final ChessWebSocketHandler handler;
    private final OpeningService openingService;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> currentReplayTask;

    public ReplayService(ChessWebSocketHandler handler, OpeningService openingService) {
        this.handler = handler;
        this.openingService = openingService;
    }

    /**
     * Generic replay entry point.
     *
     * @param opening     category (open_games, semi_open_games, etc.)
     *                    kept for future validation / evolution
     * @param openingId   opening id (ruy_lopez, italian_game, ...)
     * @param variationId variation id (CHIGORIN, BREYER, ...)
     */
    public synchronized void replay(String opening, String openingId, String variationId) {

        // Cancel current replay if running
        if (currentReplayTask != null && !currentReplayTask.isDone()) {
            currentReplayTask.cancel(true);
        }

        // Load moves from JSON (data-driven)
        List<String> moves = openingService.getVariationMoves(openingId, variationId);
        Iterator<String> iterator = moves.iterator();

        // Track move index
        AtomicInteger moveIndex = new AtomicInteger(0);

        // Schedule replay
        currentReplayTask = scheduler.scheduleAtFixedRate(() -> {
            if (!iterator.hasNext()) {
                currentReplayTask.cancel(false);
                return;
            }
            // Send each move as structured JSON instead of raw SAN string.
            // The frontend now expects a JSON payload with a "type" field
            // to differentiate between MOVE and STAT messages.
            String san = iterator.next();
            int index = moveIndex.getAndIncrement();

            // Build structured MOVE message
            String moveMessage = """
                {
                    "type": "MOVE",
                    "move": {
                        "san": "%s"
                    }
                }
               """.formatted(san);
            // Broadcast JSON message to all connected WebSocket clients
            handler.broadcast(moveMessage);

            // ===============================
            // STAT message (only first move)
            // ===============================
            if (index == 0) {

                String statMessage = """
                {
                    "type": "STAT",
                    "pauseMs": 3000,
                    "message": "This opening starts with %s"
                }
               """.formatted(san);

                handler.broadcast(statMessage);
            }

        }, 0, 1200, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdownNow();
    }
}
