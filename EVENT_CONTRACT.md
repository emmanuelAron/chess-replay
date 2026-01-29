# Chess Event Contract

This document describes the domain events exchanged through Kafka
in the Chess Replay event-driven architecture.

Kafka is used as a transport layer only.
The domain event structure is defined by the business model.

---

## Event Topic

- **Topic name:** `chess.events`
- **Key:** `gameId`
- **Value:** JSON payload representing a domain event

Using `gameId` as the Kafka key guarantees message ordering
for all moves belonging to the same game.

---

## Event: MovePlayedEvent

A `MovePlayedEvent` is published each time a chess move is played
during a replay.

This event represents a factual occurrence and is immutable.

### JSON Structure

```json
{
  "eventType": "MOVE_PLAYED",
  "eventId": "uuid",
  "timestamp": "2026-01-27T21:14:32.456Z",
  "game": {
    "gameId": "RUY_LOPEZ:CHIGORIN",
    "whitePlayer": "Kasparov",
    "blackPlayer": "Karpov",
    "event": "World Championship",
    "date": "1985-10-15"
  },
  "move": {
    "moveIndex": 12,
    "san": "Bb5"
  }
}
```
--------------------------------
## Field Description

### eventType
- Type of the domain event
- Used by consumers to filter or route events
- Example: `MOVE_PLAYED`

---

### eventId
- Unique identifier of the event
- Useful for audit, debugging and deduplication

---

### timestamp
- ISO-8601 timestamp indicating when the event occurred
- Independent from processing time

---

### game
Contains the game context.

- **gameId**: unique identifier of the game (Kafka key)
- **whitePlayer**: white player name
- **blackPlayer**: black player name
- **event**: tournament or match name
- **date**: game date

---

### move
Contains move-related information.

- **moveIndex**: index of the move in the game
- **san**: move notation in SAN format

---

## Design Principles

- Events represent **facts**, not commands
- Events are **immutable**
- Kafka does not interpret the payload
- Consumers are fully decoupled
- New consumers can be added without changing producers

---

## Future Events

The same topic may contain additional event types in the future:
