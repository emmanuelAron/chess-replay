package com.github.emmanuelAron.events;

public record StatMessage(
        String type,       // "STAT"
        String gameId,
        String message,
        int pauseMs
) {}