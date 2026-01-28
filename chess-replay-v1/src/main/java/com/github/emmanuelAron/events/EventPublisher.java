package com.github.emmanuelAron.events;

/**
 * EventPublisher is the abstraction responsible for publishing
 * domain events produced by the replay engine.
 *
 * It decouples the core replay logic from the underlying
 * transport mechanism (WebSocket, Kafka, etc.).
 *
 * Typical implementations:
 * - WebSocketEventPublisher (real-time UI updates)
 * - KafkaEventPublisher (event-driven / analytics pipeline)
 *
 * This interface is a key building block of the event-oriented architecture.
 */
public interface EventPublisher {
    /**
     * Publishes a domain event.
     *
     * @param event the event to publish (e.g. MovePlayedEvent)
     */
    void publish(Object event);
}
