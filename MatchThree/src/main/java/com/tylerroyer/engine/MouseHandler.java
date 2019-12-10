package com.tylerroyer.engine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.MouseInfo;

/**
 * Helper class for mouse input.
 */
public class MouseHandler implements MouseListener {
    private boolean isDown = false;

    MouseHandler() {}

    /**
     * @return The mouse's X-coordinate relative to the window.
     */
    public int getX() {
        int absoluteX = (int) MouseInfo.getPointerInfo().getLocation().getX();
        int windowX = Game.getWindow().getX() + 8;
        return absoluteX - windowX;
    }

    /**
     * @return The mouse's Y-coordinate relative to the window.
     */
    public int getY() {
        int absoluteY = (int) MouseInfo.getPointerInfo().getLocation().getY();
        int windowY = Game.getWindow().getY() + 31;
        return absoluteY - windowY;
    }

    /**
     * @return True if m1 is down.  False otherwise.
     */
    public boolean isDown() {
        return isDown;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {isDown = true;}

    @Override
    public void mouseReleased(MouseEvent e) {isDown = false;}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}