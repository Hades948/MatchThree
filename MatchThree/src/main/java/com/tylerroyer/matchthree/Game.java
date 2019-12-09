package com.tylerroyer.matchthree;

import java.util.ArrayList;

public class Game {
    private float lastFrameTime, timeLeftInFrame, currentFPS;
    private long frameStartTime;
    private final float TARGET_FPS = 60.0f;

    public Game() {
        running = true;

        setCurrentScreen(new GridScreen());
        particleEmitters = new ArrayList<>();
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

    private static Screen currentScreen;
    public static Screen getCurrentScreen() {
        return currentScreen;
    }
    public static void setCurrentScreen(Screen screen) {
        currentScreen = screen;
    }

    private static ArrayList<ParticleEmitter> particleEmitters;
    public static ArrayList<ParticleEmitter> getParticleEmitters() {
        return particleEmitters;
    }
    public static void addParticleEmitter(ParticleEmitter particleEmitter) {
        particleEmitters.add(particleEmitter);
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
        getCurrentScreen().update();
        for (ParticleEmitter emitter : new ArrayList<ParticleEmitter>(particleEmitters)) {
            emitter.update();
            if (!emitter.isAlive()) {
                particleEmitters.remove(emitter);
            }
        }
    }

    // Currently not being used.  It seems like the canvas is drawing in a seperate thread.
    // This may not be an issue except for CMEs.  If these start to pop up, I'll need to
    // Change the rendering a bit.  This is staying here for now just in case.
    private void show() {
    }
}
