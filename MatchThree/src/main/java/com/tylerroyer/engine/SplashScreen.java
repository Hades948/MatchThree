package com.tylerroyer.engine;

import java.awt.Color;
import java.awt.Graphics2D;

class SplashScreen extends Screen {
    private int counter = 0;
    private Screen firstScreen;

    public SplashScreen(Screen firstScreen) {
        this.firstScreen = firstScreen;
    }

    @Override
    public void loadResources() {
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
        g.setColor(Color.WHITE);
        g.drawString("Splash Screen Template", 380, 300);
    }
}