package com.tylerroyer.engine.particles;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * Randomly emits a specified number of ImageParticles from one location.
 */
public class ImageParticleEmitter extends ParticleEmitter {
    public ImageParticleEmitter(BufferedImage image, int x, int y, int count) {
        Random rand = new Random();

        particles = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ImageParticle p = new ImageParticle(image, x, y, rand.nextFloat() * 4 - 2, rand.nextFloat() * 4 - 12);
            particles.add(p);
        }
    }
}