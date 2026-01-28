package com.github.emmanuelAron.events;

public enum ReplaySpeed {

    SLOW(2000),
    NORMAL(1000),
    FAST(400);

    private final long delayMillis;

    ReplaySpeed(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    public long getDelayMillis() {
        return delayMillis;
    }
}
