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
    private Point selectedGridPoint = null;

    private ArrayList<ArrayList<Tile>> grid;

    public Grid() {
        grid = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            ArrayList<Tile> row = new ArrayList<>();
            for (int j = 0; j < SIZE; j++) {
                row.add(new Tile());
            }
            grid.add(row);
        }
    }

    public void update() {
        for (ArrayList<Tile> row : grid) {
            for (Tile tile : row) {
                tile.update();
            }
        }

        if (Game.getMouseHandler().isDown() && selectedGridPoint == null) {
            selectedGridPoint = getGridPoint(Game.getMouseHandler().getX(), Game.getMouseHandler().getY());
        }

        if (!Game.getMouseHandler().isDown() && selectedGridPoint != null) {
            Point other = getGridPoint(Game.getMouseHandler().getX(), Game.getMouseHandler().getY());
            Tile selectedTile = grid.get((int) selectedGridPoint.getX()).get((int) selectedGridPoint.getY());
            Tile otherTile = grid.get((int) other.getX()).get((int) other.getY());

            // Set directions.
            if (other.getX() == selectedGridPoint.getX() - 1 && other.getY() == selectedGridPoint.getY()) {
                // Other is to the left of selected.
                selectedTile.setDirection(Tile.Direction.LEFT);
                otherTile.setDirection(Tile.Direction.RIGHT);
            } else if (other.getX() == selectedGridPoint.getX() + 1 && other.getY() == selectedGridPoint.getY()) {
                // Other is to the right of selected.
                selectedTile.setDirection(Tile.Direction.RIGHT);
                otherTile.setDirection(Tile.Direction.LEFT);
            } else if (other.getX() == selectedGridPoint.getX() && other.getY() == selectedGridPoint.getY() - 1) {
                // Other above selected.
                selectedTile.setDirection(Tile.Direction.UP);
                otherTile.setDirection(Tile.Direction.DOWN);
            } else if (other.getX() == selectedGridPoint.getX() && other.getY() == selectedGridPoint.getY() + 1) {
                // Other below selected.
                selectedTile.setDirection(Tile.Direction.DOWN);
                otherTile.setDirection(Tile.Direction.UP);
            }

            // TODO This is where I need to switch modes.  At this point, I need to hold on to the selected and other tiles
            // TODO   and then check for grid stability once they're done moving.  If stable, revert.  Otherwise keep and break.
            // TODO   for now, though, I'll just wait for them to flip and reset to selection mode.

            grid.get((int) selectedGridPoint.getX()).set((int) selectedGridPoint.getY(), grid.get((int) other.getX()).get((int) other.getY()));
            grid.get((int) other.getX()).set((int) other.getY(), selectedTile);

            selectedGridPoint = null;
        }
    }

    public void render(Graphics2D g) {
        // Render the grid.
        for (int i = 0; i < grid.size(); i++) {
            ArrayList<Tile> row = grid.get(i);
            for (int j = 0; j < row.size(); j++) {
                g.setColor(row.get(j).getColor());
                int x = PADDING + i * (SQUARE_SIZE + PADDING) + grid.get(i).get(j).getOffsetX();
                int y = PADDING + j * (SQUARE_SIZE + PADDING) + grid.get(i).get(j).getOffsetY();
                int width = SQUARE_SIZE;
                int height = SQUARE_SIZE;
                g.fillRect(x, y, width, height);
            }
        }

        // Render the highlighter
        g.setColor(new Color(255, 255, 255, 50));
        Point gridPoint = getGridPoint(Game.getMouseHandler().getX(), Game.getMouseHandler().getY());
        int highlighterX = (int) gridPoint.getX() * (SQUARE_SIZE + PADDING) + PADDING / 2;
        int highlighterY = (int) gridPoint.getY() * (SQUARE_SIZE + PADDING) + PADDING / 2;
        g.fillRect(highlighterX, highlighterY, SQUARE_SIZE + PADDING, SQUARE_SIZE + PADDING);

        // Render the selector
        if (selectedGridPoint != null) {
            g.setColor(new Color(255, 255, 255));
            int selectorX = (int) selectedGridPoint.getX() * (SQUARE_SIZE + PADDING) + PADDING / 2;
            int selectorY = (int) selectedGridPoint.getY() * (SQUARE_SIZE + PADDING) + PADDING / 2;
            g.drawRect(selectorX, selectorY, SQUARE_SIZE + PADDING, SQUARE_SIZE + PADDING);
        }
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