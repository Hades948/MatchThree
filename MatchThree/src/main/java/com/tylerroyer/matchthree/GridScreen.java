package com.tylerroyer.matchthree;

import java.awt.Graphics2D;

public class GridScreen extends Screen {
    Grid grid;

    public GridScreen() {
        grid = new Grid();
    }

    @Override
    public void update() {
        grid.update();
    }

    @Override
    public void render(Graphics2D g) {
        grid.render(g);
    }
}