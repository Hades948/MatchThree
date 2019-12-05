package com.tylerroyer.matchthree;

public class Game {
    private float lastFrameTime, timeLeftInFrame, currentFPS;
    private long frameStartTime;
    private final float TARGET_FPS = 60.0f;

    public Game() {
        running = true;

        grid = new Grid();
        keyboardHandler = new KeyboardHandler();
        mouseHandler = new MouseHandler();
        renderer = new Renderer();
        window = new Window();

        frameStartTime = System.currentTimeMillis();
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

    private static KeyboardHandler keyboardHandler;
    public static KeyboardHandler getKeyboardHandler() {
        return keyboardHandler;
    }

    private static Grid grid;
    public static Grid getGrid() {
        return grid;
    }

    public void loop() {
        while(isRunning()) {
            frameStartTime = System.currentTimeMillis();

            update();
            show();

            // FPS Calculation
            lastFrameTime = System.currentTimeMillis() - frameStartTime;
            timeLeftInFrame = (1000 / TARGET_FPS) - lastFrameTime;
            if (timeLeftInFrame >= 0) {
                // On time.  Sleep remainder of frame.
                try { Thread.sleep((long) timeLeftInFrame); } catch (InterruptedException e) { e.printStackTrace(); }
                currentFPS = TARGET_FPS;
            } else {
                // Running behind
                currentFPS = 1000 / lastFrameTime;
            }
        }
    }

    private void update() {
        grid.update();
    }

    // Currently not being used.  It seems like the canvas is drawing in a seperate thread.
    // This may not be an issue except for CMEs.  If these start to pop up, I'll need to
    // Change the rendering a bit.  This is staying here for now just in case.
    private void show() {
    }
}
