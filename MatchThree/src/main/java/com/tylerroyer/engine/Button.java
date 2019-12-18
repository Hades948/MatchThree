package com.tylerroyer.engine;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Point;

public class Button {
    BufferedImage pressed, unpressed, highlighted;
    int x, y;

    public Button(BufferedImage pressed, BufferedImage unpressed, BufferedImage highlighted, int x, int y) {
        this.pressed = pressed;
        this.unpressed = unpressed;
        this.highlighted = highlighted;
        this.x = x;
        this.y = y;
    }

    private boolean isMouseHovering() {
        Point mouseOffset = new Point(x - Game.getMouseHandler().getX() + pressed.getWidth(), y - Game.getMouseHandler().getY() + pressed.getHeight());
        return pressed.getRaster().getBounds().contains(mouseOffset);
    }

    public boolean isPressed() {
        return isMouseHovering() && Game.getMouseHandler().isDown();
    }

    public void render(Graphics2D g) {
        if (isPressed())
            g.drawImage(pressed, x, y, Game.getWindow());
        else if (isMouseHovering())
            g.drawImage(highlighted, x, y, Game.getWindow());
        else
            g.drawImage(unpressed, x, y, Game.getWindow());
    }
}