package com.tylerroyer.matchthree;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Tile {
    private static ArrayList<Color> colors;
    private static final int SPEED = 4;

    static {
        colors = new ArrayList<>();
        final Color RED = new Color(200, 0, 0);
        final Color BLUE = new Color(0, 0, 200);
        final Color GREEN = new Color(0, 180, 0);
        final Color PURPLE = new Color(100, 0, 100);
        colors.add(RED);
        colors.add(BLUE);
        colors.add(GREEN);
        colors.add(PURPLE);
    }

    public enum Direction {UP, DOWN, LEFT, RIGHT, NONE};

    private Color color;
    private int offsetX, offsetY;
    private Direction direction;

    public Tile() {
        color = colors.get(new Random().nextInt(colors.size()));
        offsetX = offsetY = 0;
        direction = Direction.NONE;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void resetOffsets() {
        offsetX = offsetY = 0;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void reverseDirection() {
        switch (direction) {
        case UP:
            direction = Direction.DOWN;
            break;
        case DOWN:
            direction = Direction.UP;
            break;
        case LEFT:
            direction = Direction.RIGHT;
            break;
        case RIGHT:
            direction = Direction.LEFT;
            break;
        case NONE:
        default:
            break;
        }
    }

    public void update() {
        switch (direction) {
        case UP:
            offsetY -= SPEED;
            break;
        case DOWN:
            offsetY += SPEED;
            break;
        case LEFT:
            offsetX -= SPEED;
            break;
        case RIGHT:
            offsetX += SPEED;
            break;
        case NONE:
        default:
            break;
        }
    }
}