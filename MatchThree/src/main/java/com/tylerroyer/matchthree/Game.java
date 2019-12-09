package com.tylerroyer.matchthree;

import java.util.ArrayList;

public class Game {
    public static void setup() {
        running = true;

        particleEmitters = new ArrayList<>();
        keyboardHandler = new KeyboardHandler();
        mouseHandler = new MouseHandler();
        renderer = new Renderer();
        window = new Window();
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
}
