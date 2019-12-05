package com.tylerroyer.matchthree;

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
            Particle p = new Particle(color, x, y, rand.nextFloat() * 2 - 1, rand.nextFloat() * 3 - 5);
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
            // TODO Calculate status of emitter based on particles' y-values.
            if (p.getY() < Game.getRenderer().getHeight()) {
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