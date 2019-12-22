package com.tylerroyer.matchthree;

import com.tylerroyer.engine.*;
import com.tylerroyer.engine.particles.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.awt.event.KeyEvent;

public class GridScreen extends Screen {
    private final int SIZE = 10;
    private final int SQUARE_SIZE = 60;
    private final int PADDING = 10;
    private final int GRID_OFFSET_X = 79;
    private final int GRID_OFFSET_Y = 141;

    private final int BASE_CRYSTALS = 3;
    private int crystals = 0;
    private int combo = 1;

    private ArrayList<ArrayList<Tile>> grid;

    ArrayList<ParticleEmitter> particleEmitters = new ArrayList<>();

    private enum Mode {
        SELECTION, SELECTION_ANIMATION, BREAKING, GRAVITY, GRAVITY_ANIMATION
    }

    private Mode currentMode;

    private boolean invalidFlag = false;
    private boolean gameOver = false;

    private Point firstSelectedPoint = null;
    private Point secondSelectedPoint = null;
    private Tile firstSelectedTile = null;
    private Tile secondSelectedTile = null;

    private HashSet<Point> unstablePoints;
    private ArrayList<Integer> fallIndecies;
    private int tilesFalling, tilesFallen;

    private BufferedImage ambientBackground, gridBackground;
    private BufferedImage redCrystal, blueCrystal, purpleCrystal, greenCrystal;
    private BufferedImage redCrystalParticle, blueCrystalParticle, purpleCrystalParticle, greenCrystalParticle;

    private Timer timer;
    private Button pauseButton, resumeButton;
    private boolean paused;

    public void loadResources() {
        ambientBackground = Resources.loadGraphicalImage("ambient_background_1.png");
        gridBackground = Resources.loadGraphicalImage("grid_background.png");
        redCrystal = Resources.loadGraphicalImage("crystal_red.png");
        blueCrystal = Resources.loadGraphicalImage("crystal_blue.png");
        purpleCrystal = Resources.loadGraphicalImage("crystal_purple.png");
        greenCrystal = Resources.loadGraphicalImage("crystal_green.png");
        redCrystalParticle = Resources.loadGraphicalImage("crystal_red_particle.png");
        blueCrystalParticle = Resources.loadGraphicalImage("crystal_blue_particle.png");
        purpleCrystalParticle = Resources.loadGraphicalImage("crystal_purple_particle.png");
        greenCrystalParticle = Resources.loadGraphicalImage("crystal_green_particle.png");
        
        BufferedImage pausedPressed = Resources.loadGraphicalImage("pause_button_pressed.png");
        BufferedImage pausedUnpressed = Resources.loadGraphicalImage("pause_button_unpressed.png");
        BufferedImage pausedHighlighted = Resources.loadGraphicalImage("pause_button_highlighted.png");
        pauseButton = new Button(pausedPressed, pausedUnpressed, pausedHighlighted, 820, 8);

        BufferedImage resumePressed = Resources.loadGraphicalImage("resume_button_pressed.png");
        BufferedImage resumeUnpressed = Resources.loadGraphicalImage("resume_button_unpressed.png");
        BufferedImage resumeHighlighted = Resources.loadGraphicalImage("resume_button_highlighted.png");
        resumeButton = new Button(resumePressed, resumeUnpressed, resumeHighlighted, 334, 400);
        
        // Not the best place for this but it's the only place that works atm.
        timer = new Timer(1 * 60 * 1000);
        timer.start();
    }

    public GridScreen() {
        grid = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            ArrayList<Tile> row = new ArrayList<>();
            for (int j = 0; j < SIZE; j++) {
                row.add(new Tile());
            }
            grid.add(row);
        }

        fallIndecies = new ArrayList<>();
        for (int i = 0; i < grid.get(0).size(); i++) {
            fallIndecies.add(-1);
        }

        unstablePoints = new HashSet<>();

        while(!isStable()) shuffle();

        currentMode = Mode.SELECTION;
        paused = false;
    }

    @Override
    public void update() {
        if (gameOver) return;

        timer.update();

        if (paused)  {
            if (resumeButton.isDown()) {
                paused = false;
                timer.start();
            } else return;
        } else if (pauseButton.isDown()) {
            paused = true;
            timer.pause();
        }

        // ***** Update grid ***** //
        for (ArrayList<Tile> row : grid) {
            for (Tile tile : row) {
                if (tile != null) tile.update();
            }
        }

        switch (currentMode) {
        case SELECTION:
            // check for game over.
            if(timer.getTimeLeftMillis() == 0) {
                gameOver = true;
                Game.showMessage("You scored " + crystals + ".");
                Game.close();
            }
            
            // Dynamic shuffle
            if (Game.getKeyboardHandler().isKeyDown(KeyEvent.VK_S)) {
                for (int i = 0; i < 50 || !isStable(); i++) {
                    shuffle();
                }
            }

            // Selecting first tile by pressing mouse.
            if (Game.getMouseHandler().isDown() && firstSelectedPoint == null) {
                firstSelectedPoint = getGridPoint(Game.getMouseHandler().getX() - GRID_OFFSET_X, Game.getMouseHandler().getY() - GRID_OFFSET_Y);
                firstSelectedTile = grid.get((int) firstSelectedPoint.getY()).get((int) firstSelectedPoint.getX());
            }

            // Selecting second tile by releasing mouse.
            if (!Game.getMouseHandler().isDown() && firstSelectedPoint != null) {
                secondSelectedPoint = getGridPoint(Game.getMouseHandler().getX() - GRID_OFFSET_X, Game.getMouseHandler().getY() - GRID_OFFSET_Y);
                secondSelectedTile = grid.get((int) secondSelectedPoint.getY()).get((int) secondSelectedPoint.getX());

                // Set directions.
                if (secondSelectedPoint.getX() == firstSelectedPoint.getX() - 1 && secondSelectedPoint.getY() == firstSelectedPoint.getY()) {
                    // Second is to the left of first.
                    firstSelectedTile.setDirection(Tile.Direction.LEFT);
                    secondSelectedTile.setDirection(Tile.Direction.RIGHT);
                    currentMode = Mode.SELECTION_ANIMATION;
                } else if (secondSelectedPoint.getX() == firstSelectedPoint.getX() + 1 && secondSelectedPoint.getY() == firstSelectedPoint.getY()) {
                    // Second is to the right of first.
                    firstSelectedTile.setDirection(Tile.Direction.RIGHT);
                    secondSelectedTile.setDirection(Tile.Direction.LEFT);
                    currentMode = Mode.SELECTION_ANIMATION;
                } else if (secondSelectedPoint.getX() == firstSelectedPoint.getX() && secondSelectedPoint.getY() == firstSelectedPoint.getY() - 1) {
                    // Second is above first.
                    firstSelectedTile.setDirection(Tile.Direction.UP);
                    secondSelectedTile.setDirection(Tile.Direction.DOWN);
                    currentMode = Mode.SELECTION_ANIMATION;
                } else if (secondSelectedPoint.getX() == firstSelectedPoint.getX() && secondSelectedPoint.getY() == firstSelectedPoint.getY() + 1) {
                    // Second is below first.
                    firstSelectedTile.setDirection(Tile.Direction.DOWN);
                    secondSelectedTile.setDirection(Tile.Direction.UP);
                    currentMode = Mode.SELECTION_ANIMATION;
                } else {
                    // Reset because of invalid selection.
                    firstSelectedPoint = secondSelectedPoint = null;
                    firstSelectedTile = secondSelectedTile = null;
                }
            }
            break;
        case SELECTION_ANIMATION:
            // Check for the end of the flip animation.
            if (firstSelectedTile.getDirection() == Tile.Direction.LEFT && firstSelectedTile.getOffsetX() < -(SQUARE_SIZE+PADDING)
             || firstSelectedTile.getDirection() == Tile.Direction.RIGHT && firstSelectedTile.getOffsetX() > +(SQUARE_SIZE+PADDING)
             || firstSelectedTile.getDirection() == Tile.Direction.UP && firstSelectedTile.getOffsetY() < -(SQUARE_SIZE+PADDING)
             || firstSelectedTile.getDirection() == Tile.Direction.DOWN && firstSelectedTile.getOffsetY() > +(SQUARE_SIZE+PADDING)) {
                // Swap tiles.
                grid.get((int) firstSelectedPoint.getY()).set((int) firstSelectedPoint.getX(), secondSelectedTile);
                grid.get((int) secondSelectedPoint.getY()).set((int) secondSelectedPoint.getX(), firstSelectedTile);

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
                        populateUnstablePoints();
                        currentMode = Mode.BREAKING;
                    }
                }
            }

            break;
        case BREAKING:
            for (Point p : unstablePoints) {
                Point absolutePoint = getAbsolutePoint((int) p.getX(), (int) p.getY());
                absolutePoint.x += SQUARE_SIZE / 2;
                absolutePoint.y += SQUARE_SIZE / 2;
                Tile tile = grid.get((int) p.getY()).get((int) p.getX());
                if (tile.getColor() == Tile.RED) {
                    particleEmitters.add(new ImageParticleEmitter(redCrystalParticle, (int) absolutePoint.getX(), (int) absolutePoint.getY(), BASE_CRYSTALS * combo));    
                } else if (tile.getColor() == Tile.BLUE) {
                    particleEmitters.add(new ImageParticleEmitter(blueCrystalParticle, (int) absolutePoint.getX(), (int) absolutePoint.getY(), BASE_CRYSTALS * combo));    
                } else if (tile.getColor() == Tile.PURPLE) {
                    particleEmitters.add(new ImageParticleEmitter(purpleCrystalParticle, (int) absolutePoint.getX(), (int) absolutePoint.getY(), BASE_CRYSTALS * combo));
                } else if (tile.getColor() == Tile.GREEN) {
                    particleEmitters.add(new ImageParticleEmitter(greenCrystalParticle, (int) absolutePoint.getX(), (int) absolutePoint.getY(), BASE_CRYSTALS * combo));    
                }grid.get((int) p.getY()).set((int) p.getX(), null);
                crystals += BASE_CRYSTALS * combo;
            }

            unstablePoints.clear();
            currentMode = Mode.GRAVITY;
            break;
        case GRAVITY:
            boolean isGridFull = true;
            for (int column = 0; column < grid.get(0).size(); column++) {
                for (int row = grid.size() - 1; row >= 0; row--) {
                    if (grid.get(row).get(column) == null) {
                        fallIndecies.set(column, row);
                        isGridFull = false;
                        break;
                    }
                }
            }

            if (isGridFull) {
                if (isStable()) {
                    currentMode = Mode.SELECTION;
                    combo = 1;
                } else {
                    populateUnstablePoints();
                    combo = Math.min(combo + 1, 4);
                    currentMode = Mode.BREAKING;
                }
            } else {
                tilesFalling = tilesFallen = 0;
                currentMode = Mode.GRAVITY_ANIMATION;
            }
            break;
        case GRAVITY_ANIMATION:
            for (int column = 0; column < grid.get(0).size(); column++) {
                if (fallIndecies.get(column) > -1) {
                    for (int row = fallIndecies.get(column) - 1; row >= 0 ; row--) {
                        Tile tile = grid.get(row).get(column);
                        if (tile == null) continue;

                        if (tile.getDirection() == Tile.Direction.NONE) {
                            // If this tile is not already falling, set its direction to down.
                            tile.setDirection(Tile.Direction.DOWN);
                            tilesFalling++;
                        } else if (tile.getDirection() == Tile.Direction.DOWN
                              && tile.getOffsetY() >= SQUARE_SIZE+PADDING) {
                            // This tile has fallen the length of one tile.
                            grid.get(row+1).set(column, tile);
                            grid.get(row).set(column, null);
                            tile.resetOffsets();
                            tile.setDirection(Tile.Direction.NONE);
                            tilesFallen++;
                        }
                    }
                }
            }

            if (tilesFalling == tilesFallen) {
                for (int i = 0; i < grid.get(0).size(); i++) {
                    // If there were tiles falling in this column, add a new one to the top.
                    if (fallIndecies.get(i) > -1) {
                        grid.get(0).set(i, new Tile());
                    }
                    fallIndecies.set(i, -1);
                }

                currentMode = Mode.GRAVITY;
            }
            break;
        }

        // ***** Update Emitters ***** //
        for (ParticleEmitter emitter : new ArrayList<ParticleEmitter>(particleEmitters)) {
            emitter.update();
            if (!emitter.isAlive()) {
                particleEmitters.remove(emitter);
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        // ***** Render background ***** //
        g.drawImage(ambientBackground, 0, 0, Game.getWindow());

        if (paused) {
            // Paused text
            g.setFont(new Font("Arial", Font.PLAIN, 48));
            g.setColor(Color.BLACK);
            g.drawString("PAUSED", 343, 373);
            g.setColor(Color.WHITE);
            g.drawString("PAUSED", 340, 370);

            // Resume button
            resumeButton.render(g);
        } else {
            // ***** Render grid ***** //
            // Grid background
            g.drawImage(gridBackground, 0, 0, Game.getWindow());
            
            // Render the grid.
            for (int rowIndex = 0; rowIndex < grid.size(); rowIndex++) {
                ArrayList<Tile> row = grid.get(rowIndex);
                for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
                    Tile tile = row.get(columnIndex);
                    if (tile != null) {
                        int x = PADDING + columnIndex * (SQUARE_SIZE + PADDING) + tile.getOffsetX() + GRID_OFFSET_X;
                        int y = PADDING + rowIndex * (SQUARE_SIZE + PADDING) + tile.getOffsetY() + GRID_OFFSET_Y;
                        if (tile.getColor() == Tile.RED) {
                            g.drawImage(redCrystal, x, y, Game.getWindow());
                        } else if (tile.getColor() == Tile.BLUE) {
                            g.drawImage(blueCrystal, x, y, Game.getWindow());
                        } else if (tile.getColor() == Tile.PURPLE) {
                            g.drawImage(purpleCrystal, x, y, Game.getWindow());
                        } else if (tile.getColor() == Tile.GREEN) {
                            g.drawImage(greenCrystal, x, y, Game.getWindow());
                        }
                    }
                }
            }

            // Render the highlighter
            g.setColor(new Color(255, 255, 255, 50));
            Point gridPoint = getGridPoint(Game.getMouseHandler().getX() - GRID_OFFSET_X, Game.getMouseHandler().getY() - GRID_OFFSET_Y);
            int highlighterX = (int) gridPoint.getX() * (SQUARE_SIZE + PADDING) + PADDING / 2 + GRID_OFFSET_X;
            int highlighterY = (int) gridPoint.getY() * (SQUARE_SIZE + PADDING) + PADDING / 2 + GRID_OFFSET_Y;
            g.fillRect(highlighterX, highlighterY, SQUARE_SIZE + PADDING, SQUARE_SIZE + PADDING);

            // Render the selector
            if (currentMode == Mode.SELECTION && firstSelectedPoint != null) {
                g.setColor(new Color(255, 255, 255));
                int selectorX = (int) firstSelectedPoint.getX() * (SQUARE_SIZE + PADDING) + PADDING / 2 + GRID_OFFSET_X;
                int selectorY = (int) firstSelectedPoint.getY() * (SQUARE_SIZE + PADDING) + PADDING / 2 + GRID_OFFSET_Y;
                g.drawRect(selectorX, selectorY, SQUARE_SIZE + PADDING, SQUARE_SIZE + PADDING);
            }

            // ***** Render overlay ***** //
            g.setColor(new Color(200, 200, 255));
            g.setFont(new Font("Arial", Font.PLAIN, 48)); 
            NumberFormat format = NumberFormat.getInstance();
            format.setGroupingUsed(true);
            g.drawString("Crystals: " + format.format(crystals), 90, 110);
            g.drawString("Combo: x" + combo, 550, 110);

            // ***** Render emitters ***** //
            for (ParticleEmitter emitter : particleEmitters) {
                emitter.render(g);
            }

            // ***** Render timer ***** //
            g.drawString(String.format("%.0f", Math.ceil(timer.getTimeLeftMillis() / 1000.0)), 10, 50);
            
            // ***** Render buttons ***** //
            pauseButton.render(g);
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
        for (int rowIndex = 0; rowIndex < grid.size(); rowIndex++) {
            ArrayList<Tile> row = grid.get(rowIndex);
            for (int columnIndex = 0; columnIndex < row.size() - 2; columnIndex++) {
                if (row.get(columnIndex).getColor() != Color.WHITE
                 && row.get(columnIndex).getColor() == row.get(columnIndex+1).getColor()
                 && row.get(columnIndex+1).getColor() == row.get(columnIndex+2).getColor()) {
                    unstablePoints.add(new Point(columnIndex, rowIndex));
                    unstablePoints.add(new Point(columnIndex+1, rowIndex));
                    unstablePoints.add(new Point(columnIndex+2, rowIndex));
                }
            }
        }

        // Vertical
        for (int column = 0; column < grid.get(0).size(); column++) {
            for (int row = 0; row < grid.size() - 2; row++) {
                if (grid.get(row).get(column).getColor() != Color.WHITE
                 && grid.get(row).get(column).getColor() == grid.get(row+1).get(column).getColor()
                 && grid.get(row+1).get(column).getColor() == grid.get(row+2).get(column).getColor()) {
                     unstablePoints.add(new Point(column, row));
                     unstablePoints.add(new Point(column, row+1));
                     unstablePoints.add(new Point(column, row+2));
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
        int x = gridX * (SQUARE_SIZE + PADDING) + PADDING + GRID_OFFSET_X;
        int y = gridY * (SQUARE_SIZE + PADDING) + PADDING + GRID_OFFSET_Y;

        return new Point(x, y);
    }
}