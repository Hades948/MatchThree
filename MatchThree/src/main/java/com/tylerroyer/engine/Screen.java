package com.tylerroyer.engine;

import java.awt.Graphics2D;

public abstract class Screen {
    public abstract void loadResources();
    public abstract void update();
    public abstract void render(Graphics2D g);
}