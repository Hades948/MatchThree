package com.tylerroyer.engine;

public class Timer {
    private long time, elapsed, timeOfLastUpdate;
    private boolean running;

    public Timer(long time) {
        this.time = time;
        elapsed = 0;
        timeOfLastUpdate = System.currentTimeMillis();
        running = false;
    }

    public Timer start() {
        running = true;
        return this;
    }

    public Timer pause() {
        running = false;
        return this;
    }

    public void update() {
        if(running) {
            long now = System.currentTimeMillis();
            elapsed += (now - timeOfLastUpdate);
            timeOfLastUpdate = now;
        }
    }

    public long getTimeLeftMillis() {
        return Math.max(time - elapsed, 0);
    }
}