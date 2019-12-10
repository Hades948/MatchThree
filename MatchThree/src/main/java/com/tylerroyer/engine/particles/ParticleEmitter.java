package com.tylerroyer.engine.particles;

import com.tylerroyer.engine.Game;

import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class ParticleEmitter {
    private boolean alive = true;
    protected ArrayList<Particle> particles;

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