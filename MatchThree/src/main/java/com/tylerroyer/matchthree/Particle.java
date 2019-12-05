package com.tylerroyer.matchthree;

import java.awt.Color;
import java.awt.Graphics2D;

public class Particle {
    private static final float GRAVITY = 0.06f;
    private static final int SIZE = 15;

    private Color color;
    private float x, y;
    private float velX, velY;

    public Particle(Color color, int initialX, int initialY, float initialVelX, float initialVelY) {
        this.color = color;
        this.x = initialX;
        this.x -= SIZE / 2; // Adjusts for the center.
        this.y = initialY;
        this.velX = initialVelX;
        this.velY = initialVelY;
    }

    public void update() {
        velY += GRAVITY;

        x += velX;
        y += velY;
    }

    public int getY() {
        return (int) y;
    }

    public void render(Graphics2D g) {
        g.setColor(color);
        g.fillRect((int) x, (int) y, SIZE, SIZE);
    }
}