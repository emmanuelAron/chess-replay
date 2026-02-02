package com.github.emmanuelAron.events;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class NoOpEventPublisher implements EventPublisher {

    @Override
    public void publish(Object event) {
        // intentionally empty
    }
}
