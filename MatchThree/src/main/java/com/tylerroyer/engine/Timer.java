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

    public void start() {
        running = true;
    }

    public void pause() {
        running = false;
    }

    public void update() {
        long now = System.currentTimeMillis();
        if(running) {
            elapsed += (now - timeOfLastUpdate);
        }
        timeOfLastUpdate = now;
    }

    public long getTimeLeftMillis() {
        return Math.max(time - elapsed, 0);
    }
}