package com.github.emmanuelAron.service;

import com.github.emmanuelAron.events.MovePlayedEvent;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Temporary implementation loading moves from static data.
 * Will later be replaced by Mongo / Kafka / PGN loaders.
 */
@Service
public class StaticGameLoader implements GameLoader {

    @Override
    public List<MovePlayedEvent> loadGame(String gameId) {
        // TODO: load from JSON / OpeningService
        return List.of(); // placeholder
    }
}
