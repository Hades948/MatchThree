package com.tylerroyer.engine;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.io.*;

import javax.imageio.ImageIO;

public class Resources {
    private static HashMap<String, BufferedImage> graphical;
    private static final String GRAPHICAL_PATH = "/res/";

    public static void init() {
        graphical = new HashMap<>();
    }

    static void unloadAllResources() {
        graphical.clear();
    }

    public static BufferedImage loadGraphicalImage(String name) {
        try {
            graphical.put(name, ImageIO.read(Resources.class.getResourceAsStream(GRAPHICAL_PATH + name)));
        } catch (IOException e) {e.printStackTrace();}

        return getGraphicalResource(name);
    }

    public static BufferedImage getGraphicalResource(String name) {
        return graphical.get(name);
    }
}