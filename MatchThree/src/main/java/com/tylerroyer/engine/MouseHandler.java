package com.tylerroyer.engine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.MouseInfo;

public class MouseHandler implements MouseListener {
    private boolean isDown = false; // TODO This probably shouldn't start as false :/

    public MouseHandler() {}

    public int getX() {
        int absoluteX = (int) MouseInfo.getPointerInfo().getLocation().getX();
        int windowX = Game.getWindow().getX() + 8;
        return absoluteX - windowX;
    }

    public int getY() {
        int absoluteY = (int) MouseInfo.getPointerInfo().getLocation().getY();
        int windowY = Game.getWindow().getY() + 31;
        return absoluteY - windowY;
    }

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