package com.tylerroyer.matchthree;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Grid {
    private int SIZE = 10;
    private int SQUARE_SIZE = 60;
    private int PADDING = 10;

    private ArrayList<Color> colors;
    private ArrayList<ArrayList<Color>> grid;

    public Grid() {
        colors = new ArrayList<>();
        final Color RED = new Color(200, 0, 0);
        final Color BLUE = new Color(0, 0, 200);
        final Color GREEN = new Color(0, 180, 0);
        final Color PURPLE = new Color(100, 0, 100);
        colors.add(RED);
        colors.add(BLUE);
        colors.add(GREEN);
        colors.add(PURPLE);

        grid = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            ArrayList<Color> row = new ArrayList<>();
            for (int j = 0; j < SIZE; j++) {
                row.add(colors.get(new Random().nextInt(colors.size())));
            }
            grid.add(row);
        }
    }

    public void render(Graphics2D g) {
        // Render the grid.
        for (int i = 0; i < grid.size(); i++) {
            ArrayList<Color> row = grid.get(i);
            for (int j = 0; j < row.size(); j++) {
                g.setColor(row.get(j));
                int x = PADDING + i * (SQUARE_SIZE + PADDING);
                int y = PADDING + j * (SQUARE_SIZE + PADDING);
                int width = SQUARE_SIZE;
                int height = SQUARE_SIZE;
                g.fillRect(x, y, width, height);
            }
        }

        // Render the selector
        g.setColor(new Color(255, 255, 255, 100));
        Point gridPoint = getGridPoint(Game.getMouseHandler().getX(), Game.getMouseHandler().getY());
        int selectorX = (int) gridPoint.getX() * (SQUARE_SIZE + PADDING) + PADDING / 2;
        int selectorY = (int) gridPoint.getY() * (SQUARE_SIZE + PADDING) + PADDING / 2;
        g.fillRect(selectorX, selectorY, SQUARE_SIZE + PADDING, SQUARE_SIZE + PADDING);
    }

    private Point getGridPoint(int x, int y) {
        int gridX = (x - 5) / (SQUARE_SIZE + PADDING);
        int gridY = (y - 5) / (SQUARE_SIZE + PADDING);

        if (gridX < 0) gridX = 0;
        if (gridY < 0) gridY = 0;
        if (gridX > SIZE - 1) gridX = SIZE - 1;
        if (gridY > SIZE - 1) gridY = SIZE - 1;

        return new Point(gridX, gridY);
    }
}