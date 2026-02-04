package com.github.emmanuelAron.service;

import com.github.emmanuelAron.events.ReplaySpeed;
import com.github.emmanuelAron.model.Opening;
import com.github.emmanuelAron.model.Variation;
import com.github.emmanuelAron.websocket.ChessWebSocketHandler;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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

//    public synchronized void replayEspagnole(String openingId, String variationId) {
//        if (currentReplayTask != null && !currentReplayTask.isDone()) {
//            currentReplayTask.cancel(true);
//        }
//        List<String> moves =
//                openingService.getEspagnoleVariationMoves(openingId, variationId);
//
//        Iterator<String> iterator = moves.iterator();
//
//        currentReplayTask = scheduler.scheduleAtFixedRate(() -> {
//            if (!iterator.hasNext()) {
//                currentReplayTask.cancel(false);
//                return;
//            }
//            handler.broadcast(iterator.next());
//        }, 0, 1200, TimeUnit.MILLISECONDS);
//    }
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
        List<String> moves =
                openingService.getVariationMoves(openingId, variationId);

        Iterator<String> iterator = moves.iterator();

        // Schedule replay
        currentReplayTask = scheduler.scheduleAtFixedRate(() -> {
            if (!iterator.hasNext()) {
                currentReplayTask.cancel(false);
                return;
            }
            handler.broadcast(iterator.next());
        }, 0, 1200, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdownNow();
    }
}
