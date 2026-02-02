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

    public synchronized void replayEspagnole(String openingId, String variationId) {
        if (currentReplayTask != null && !currentReplayTask.isDone()) {
            currentReplayTask.cancel(true);
        }
        List<String> moves =
                openingService.getEspagnoleVariationMoves(openingId, variationId);

        Iterator<String> iterator = moves.iterator();

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
