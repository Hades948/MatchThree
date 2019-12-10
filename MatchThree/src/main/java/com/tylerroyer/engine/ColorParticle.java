package com.tylerroyer.engine;

import java.awt.Color;
import java.awt.Graphics2D;

public class ColorParticle extends Particle {
    private static final int SIZE = 15;
    private Color color;

    ColorParticle(Color color, int initialX, int initialY, float initialVelX, float initialVelY) {
        super(initialX, initialY, initialVelX, initialVelY);
        this.color = color;
    }

    @Override
    void render(Graphics2D g) {
        g.setColor(color);
        g.fillRect((int) x, (int) y, SIZE, SIZE);
        g.setColor(Color.BLACK);
        g.drawRect((int) x, (int) y, SIZE, SIZE);
    }
}