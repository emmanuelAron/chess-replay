package com.github.emmanuelAron.events;

import java.time.Instant;

public record MovePlayedEvent(
        String eventType,
        String eventId,
        Instant timestamp,
        GameInfo game,
        MoveInfo move
) {}
