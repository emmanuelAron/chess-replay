package com.github.emmanuelAron.events;

public record GameInfo(
        String gameId,
        String whitePlayer,
        String blackPlayer,
        String event,
        String date
) {}
