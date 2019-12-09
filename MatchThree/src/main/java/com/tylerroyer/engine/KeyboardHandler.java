package com.tylerroyer.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class KeyboardHandler implements KeyListener {
    private HashSet<Integer> downKeys;

    KeyboardHandler() {
        downKeys = new HashSet<>();
    }

    public boolean isKeyDown(int keyCode) {
        return downKeys.contains(keyCode);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        downKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        downKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}    
}