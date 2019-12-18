package com.tylerroyer.engine;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

/**
 * A basic splash screen to play at the launch of the game.
 */
class SplashScreen extends Screen {
    private int counter = 0;
    private Screen firstScreen;
    private BufferedImage splash;

    /**
     * @param firstScreen The screen to switch to after the splash screen has finished.
     */
    public SplashScreen(Screen firstScreen) {
        this.firstScreen = firstScreen;
    }

    @Override
    public void loadResources() {
        splash = Resources.loadEngineGraphicalImage("engine_splash.png");
    }

    @Override
    public void update() {
        counter++;

        if (counter >= 2*60) {
            Game.setCurrentScreen(firstScreen);
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(splash, 0, 0, Game.getWindow());
    }
}