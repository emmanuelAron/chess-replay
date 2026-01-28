package com.github.emmanuelAron.service;

import com.github.emmanuelAron.events.ReplayMode;
import com.github.emmanuelAron.events.ReplaySpeed;

// service/ReplayEngine.java
public interface ReplayEngine {

    void replayGame(
            String gameId,
            ReplayMode mode,
            ReplaySpeed speed
    );
}
