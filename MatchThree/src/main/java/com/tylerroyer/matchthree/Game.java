package com.tylerroyer.matchthree;

import java.util.ArrayList;

public class Game {
    public static void init() {
        keyboardHandler = new KeyboardHandler();
        mouseHandler = new MouseHandler();
        looper = new Looper();
        window = new Window();
    }

    private static Window window;
    public static Window getWindow() {
        return window;
    }

    private static Looper looper;
    public static Looper getLooper() {
        return looper;
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
}
