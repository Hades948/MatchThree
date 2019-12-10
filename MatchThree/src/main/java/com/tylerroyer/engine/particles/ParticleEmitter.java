package com.tylerroyer.engine.particles;

import com.tylerroyer.engine.Game;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Randomly emits a specified number of particles from one location.
 */
public abstract class ParticleEmitter {
    private boolean alive = true;
    protected ArrayList<Particle> particles;

    /**
     * @return True if at least one particle is still on screen.  False otherwise.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Updates all particles and checks state.
     */
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

    /**
     * Renders all particles
     * @param g The graphics to render to.
     */
    public void render(Graphics2D g) {
        for (Particle p : particles) {
            p.render(g);
        }
    }
}