package com.tylerroyer.engine;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.io.*;

import javax.imageio.ImageIO;

/**
 * Helper class for resource IO.
 */
public class Resources {
    private static HashMap<String, BufferedImage> graphical;
    private static final String GRAPHICAL_PATH = "/res/";

    public static void init() {
        graphical = new HashMap<>();
    }

    /**
     * Clear resources from the heap.
     */
    static void unloadAllResources() {
        graphical.clear();
    }

    /**
     * Loads a graphical image onto the heap.
     * @param name The name of the image file.
     * @return A pointer to the loaded image for convenience.
     */
    public static BufferedImage loadGraphicalImage(String name) {
        try {
            graphical.put(name, ImageIO.read(Resources.class.getResourceAsStream(GRAPHICAL_PATH + name)));
        } catch (IOException e) {e.printStackTrace();}

        return getGraphicalResource(name);
    }

    /**
     * @param name The name of the image file.
     * @return A pointer to the loaded image with the given name.
     */
    public static BufferedImage getGraphicalResource(String name) {
        return graphical.get(name);
    }
}