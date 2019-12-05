package com.tylerroyer.matchthree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;

public class Grid {
    private enum Mode {SELECTION, STABILITY_CHECK, BREAKING}
    private Mode currentMode;

    private boolean invalidFlag = false;

    private int SIZE = 10;
    private int SQUARE_SIZE = 60;
    private int PADDING = 10;

    private Point firstSelectedPoint = null;
    private Point secondSelectedPoint = null;
    private Tile firstSelectedTile = null;
    private Tile secondSelectedTile = null;

    private HashSet<Point> unstablePoints;

    private ArrayList<ArrayList<Tile>> grid;

    public Grid() {
        grid = new ArrayList<>();
        unstablePoints = new HashSet<>();
        for (int i = 0; i < SIZE; i++) {
            ArrayList<Tile> row = new ArrayList<>();
            for (int j = 0; j < SIZE; j++) {
                row.add(new Tile());
            }
            grid.add(row);
        }

        while(!isStable()) shuffle();

        currentMode = Mode.SELECTION;
    }

    public void update() {
        for (ArrayList<Tile> row : grid) {
            for (Tile tile : row) {
                tile.update();
            }
        }

        switch (currentMode) {
        case SELECTION:
            // Dynamic shuffle
            if (Game.getKeyboardHandler().isKeyDown(KeyEvent.VK_S)) {
                for (int i = 0; i < 50 || !isStable(); i++) {
                    shuffle();
                }
            }

            // Selecting first tile by pressing mouse.
            if (Game.getMouseHandler().isDown() && firstSelectedPoint == null) {
                firstSelectedPoint = getGridPoint(Game.getMouseHandler().getX(), Game.getMouseHandler().getY());
                firstSelectedTile = grid.get((int) firstSelectedPoint.getX()).get((int) firstSelectedPoint.getY());
            }

            // Selecting second tile by releasing mouse.
            if (!Game.getMouseHandler().isDown() && firstSelectedPoint != null) {
                secondSelectedPoint = getGridPoint(Game.getMouseHandler().getX(), Game.getMouseHandler().getY());
                secondSelectedTile = grid.get((int) secondSelectedPoint.getX()).get((int) secondSelectedPoint.getY());

                // Set directions.
                if (secondSelectedPoint.getX() == firstSelectedPoint.getX() - 1 && secondSelectedPoint.getY() == firstSelectedPoint.getY()) {
                    // Second is to the left of first.
                    firstSelectedTile.setDirection(Tile.Direction.LEFT);
                    secondSelectedTile.setDirection(Tile.Direction.RIGHT);
                    currentMode = Mode.STABILITY_CHECK;
                } else if (secondSelectedPoint.getX() == firstSelectedPoint.getX() + 1 && secondSelectedPoint.getY() == firstSelectedPoint.getY()) {
                    // Second is to the right of first.
                    firstSelectedTile.setDirection(Tile.Direction.RIGHT);
                    secondSelectedTile.setDirection(Tile.Direction.LEFT);
                    currentMode = Mode.STABILITY_CHECK;
                } else if (secondSelectedPoint.getX() == firstSelectedPoint.getX() && secondSelectedPoint.getY() == firstSelectedPoint.getY() - 1) {
                    // Second is above first.
                    firstSelectedTile.setDirection(Tile.Direction.UP);
                    secondSelectedTile.setDirection(Tile.Direction.DOWN);
                    currentMode = Mode.STABILITY_CHECK;
                } else if (secondSelectedPoint.getX() == firstSelectedPoint.getX() && secondSelectedPoint.getY() == firstSelectedPoint.getY() + 1) {
                    // Second is below first.
                    firstSelectedTile.setDirection(Tile.Direction.DOWN);
                    secondSelectedTile.setDirection(Tile.Direction.UP);
                    currentMode = Mode.STABILITY_CHECK;
                } else {
                    // Reset because of invalid selection.
                    firstSelectedPoint = secondSelectedPoint = null;
                    firstSelectedTile = secondSelectedTile = null;
                }
            }
            break;
        case STABILITY_CHECK:
            // Check for the end of the flip animation.
            if (firstSelectedTile.getDirection() == Tile.Direction.LEFT && firstSelectedTile.getOffsetX() < -(SQUARE_SIZE+PADDING)
             || firstSelectedTile.getDirection() == Tile.Direction.RIGHT && firstSelectedTile.getOffsetX() > +(SQUARE_SIZE+PADDING)
             || firstSelectedTile.getDirection() == Tile.Direction.UP && firstSelectedTile.getOffsetY() < -(SQUARE_SIZE+PADDING)
             || firstSelectedTile.getDirection() == Tile.Direction.DOWN && firstSelectedTile.getOffsetY() > +(SQUARE_SIZE+PADDING)) {
                // Swap tiles.
                grid.get((int) firstSelectedPoint.getX()).set((int) firstSelectedPoint.getY(), secondSelectedTile);
                grid.get((int) secondSelectedPoint.getX()).set((int) secondSelectedPoint.getY(), firstSelectedTile);

                // Reset tile movement.
                firstSelectedTile.resetOffsets();
                secondSelectedTile.resetOffsets();

                boolean stable = isStable();
                if (stable && !invalidFlag) {
                    // Invalid move
                    invalidFlag = true;
                    
                    Point temp = firstSelectedPoint;
                    firstSelectedPoint = secondSelectedPoint;
                    secondSelectedPoint = temp;

                    firstSelectedTile.reverseDirection();
                    secondSelectedTile.reverseDirection();
                } else {
                    // Clear selection and stop tiles.
                    firstSelectedTile.setDirection(Tile.Direction.NONE);
                    secondSelectedTile.setDirection(Tile.Direction.NONE);
                    firstSelectedPoint = secondSelectedPoint = null;
                    firstSelectedTile = secondSelectedTile = null;

                    invalidFlag = false;

                    if (stable) {
                        currentMode = Mode.SELECTION;
                    } else {
                        currentMode = Mode.BREAKING;
                    }
                }
            }

            break;
        case BREAKING:
            populateUnstablePoints();

            for (Point p : unstablePoints) {
                Point absolutePoint = getAbsolutePoint((int) p.getX(), (int) p.getY());
                absolutePoint.x += SQUARE_SIZE / 2;
                absolutePoint.y += SQUARE_SIZE / 2;
                Tile tile = grid.get((int) p.getX()).get((int) p.getY());
                Game.addParticleEmitter(new ParticleEmitter(tile.getColor(), (int) absolutePoint.getX(), (int) absolutePoint.getY(), 20));
                tile.setColor(Game.getRenderer().getBackground());
            }

            unstablePoints.clear();
            currentMode = Mode.SELECTION;
            break;
        }
    }

    private boolean isStable() {
        // Horizontal stability.
        for (ArrayList<Tile> row : grid) {
            for (int i = 0; i < row.size() - 2; i++) {
                if (row.get(i).getColor() == row.get(i+1).getColor() && row.get(i+1).getColor() == row.get(i+2).getColor()) {
                    return false;
                }
            }
        }

        // Vertical stability.
        for (int column = 0; column < grid.get(0).size(); column++) {
            for (int row = 0; row < grid.size() - 2; row++) {
                if (grid.get(row).get(column).getColor() == grid.get(row+1).get(column).getColor()
                 && grid.get(row+1).get(column).getColor() == grid.get(row+2).get(column).getColor()) {
                     return false;
                 }
            }
        }

        return true;
    }

    private void populateUnstablePoints() {
        // Horizontal
        for (int i = 0; i < grid.size(); i++) {
            ArrayList<Tile> row = grid.get(i);
            for (int j = 0; j < row.size() - 2; j++) {
                if (row.get(j).getColor() == row.get(j+1).getColor() && row.get(j+1).getColor() == row.get(j+2).getColor()) {
                    unstablePoints.add(new Point(i, j));
                    unstablePoints.add(new Point(i, j+1));
                    unstablePoints.add(new Point(i, j+2));
                }
            }
        }

        // Vertical
        for (int column = 0; column < grid.get(0).size(); column++) {
            for (int row = 0; row < grid.size() - 2; row++) {
                if (grid.get(row).get(column).getColor() == grid.get(row+1).get(column).getColor()
                 && grid.get(row+1).get(column).getColor() == grid.get(row+2).get(column).getColor()) {
                     unstablePoints.add(new Point(row, column));
                     unstablePoints.add(new Point(row+1, column));
                     unstablePoints.add(new Point(row+2, column));
                 }
            }
        }
    }

    private void shuffle() {
        Random rand = new Random();
        int x1 = rand.nextInt(10);
        int y1 = rand.nextInt(10);
        int x2 = rand.nextInt(10);
        int y2 = rand.nextInt(10);

        Tile t1 = grid.get(x1).get(y1);
        Tile t2 = grid.get(x2).get(y2);

        grid.get(x1).set(y1, t2);
        grid.get(x2).set(y2, t1);
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
        if (currentMode == Mode.SELECTION && firstSelectedPoint != null) {
            g.setColor(new Color(255, 255, 255));
            int selectorX = (int) firstSelectedPoint.getX() * (SQUARE_SIZE + PADDING) + PADDING / 2;
            int selectorY = (int) firstSelectedPoint.getY() * (SQUARE_SIZE + PADDING) + PADDING / 2;
            g.drawRect(selectorX, selectorY, SQUARE_SIZE + PADDING, SQUARE_SIZE + PADDING);
        }
    }

    // Maps absolute point relative to canvas to a grid point.
    private Point getGridPoint(int x, int y) {
        int gridX = (x - 5) / (SQUARE_SIZE + PADDING);
        int gridY = (y - 5) / (SQUARE_SIZE + PADDING);

        if (gridX < 0) gridX = 0;
        if (gridY < 0) gridY = 0;
        if (gridX > SIZE - 1) gridX = SIZE - 1;
        if (gridY > SIZE - 1) gridY = SIZE - 1;

        return new Point(gridX, gridY);
    }

    // Maps grid point to absolute point relative to canvas
    private Point getAbsolutePoint(int gridX, int gridY) {
        int x = gridX * (SQUARE_SIZE + PADDING) + PADDING;
        int y = gridY * (SQUARE_SIZE + PADDING) + PADDING;

        return new Point(x, y);
    }
}