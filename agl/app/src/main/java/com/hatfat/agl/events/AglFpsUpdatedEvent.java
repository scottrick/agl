package com.hatfat.agl.events;

public class AglFpsUpdatedEvent {
    private final int fps;

    public AglFpsUpdatedEvent(final int fps) {
        this.fps = fps;
    }

    public int getFps() {
        return fps;
    }
}
