package com.tylerroyer.engine.particles;

import java.awt.Graphics2D;

abstract class Particle {
    private static final float GRAVITY = 0.4f;

    protected float x, y;
    private float velX, velY;

    Particle(int initialX, int initialY, float initialVelX, float initialVelY) {
        this.x = initialX;
        this.y = initialY;
        this.velX = initialVelX;
        this.velY = initialVelY;
    }

    void update() {
        velY += GRAVITY;

        x += velX;
        y += velY;
    }

    int getY() {
        return (int) y;
    }

    abstract void render(Graphics2D g);
}