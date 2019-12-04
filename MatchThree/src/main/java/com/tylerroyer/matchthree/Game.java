package com.tylerroyer.matchthree;

import java.util.Random;
import java.awt.Color;

public class Game {
    Grid grid;

    public Game() {
        running = true;

        grid = new Grid();
        renderer = new Renderer(grid);
        window = new Window();
        mouseHandler = new MouseHandler();
    }

    private static boolean running;
    public static boolean isRunning() {
        return running;
    }
    public static void stop() {
        running = false;
    }

    private static Window window;
    public static Window getWindow() {
        return window;
    }

    private static Renderer renderer;
    public static Renderer getRenderer() {
        return renderer;
    }

    private static MouseHandler mouseHandler;
    public static MouseHandler getMouseHandler() {
        return mouseHandler;
    }

    public void loop() {
        while(isRunning()) {
            update();
            show();
        }
    }

    private void update() {
        
    }

    private void show() {
    }
}
