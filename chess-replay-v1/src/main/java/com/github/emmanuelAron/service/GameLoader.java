package com.github.emmanuelAron.service;

import com.github.emmanuelAron.events.MovePlayedEvent;

import java.util.List;

/**
 * Loads a chess game moves from any source (JSON, PGN, DB, Kafka, etc.).
 *
 * ReplayEngine relies on this abstraction to stay source-agnostic.
 */
public interface GameLoader {

    List<MovePlayedEvent> loadGame(String gameId);
}
