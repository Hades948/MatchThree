package com.tylerroyer.engine;

import java.awt.Color;
import java.awt.Graphics2D;

public class Particle {
    private static final float GRAVITY = 0.4f;
    private static final int SIZE = 15;

    private Color color;
    private float x, y;
    private float velX, velY;

    Particle(Color color, int initialX, int initialY, float initialVelX, float initialVelY) {
        this.color = color;
        this.x = initialX;
        this.x -= SIZE / 2; // Adjusts for the center.
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

    void render(Graphics2D g) {
        g.setColor(color);
        g.fillRect((int) x, (int) y, SIZE, SIZE);
        g.setColor(Color.BLACK);
        g.drawRect((int) x, (int) y, SIZE, SIZE);
    }
}