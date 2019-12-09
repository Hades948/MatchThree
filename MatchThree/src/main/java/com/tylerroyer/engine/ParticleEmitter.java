package com.tylerroyer.engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

public class ParticleEmitter {
    private boolean alive = true;
    private Color color;
    private int x, y;
    private ArrayList<Particle> particles;

    public ParticleEmitter(Color color, int x, int y, int count) {
        particles = new ArrayList<>();
        this.color = color;
        this.x = x;
        this.y = y;

        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            Particle p = new Particle(color, x, y, rand.nextFloat() * 4 - 2, rand.nextFloat() * 4 - 12);
            particles.add(p);
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void update() {
        boolean alive = false;

        for (Particle p : particles) {
            p.update();
            if (p.getY() < Game.getWindow().getHeight()) {
                alive = true;
            }
        }

        if (!alive)
            this.alive = false;
    }

    public void render(Graphics2D g) {
        for (Particle p : particles) {
            p.render(g);
        }
    }
}