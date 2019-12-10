package com.tylerroyer.engine;

import javax.swing.JFrame;
import java.awt.BorderLayout;

/**
 * Provides basic window for the game.
 */
public class Window extends JFrame {
    private static final long serialVersionUID = 4159603562974028158L;

    Window(String title) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(title);
        getContentPane().add(Game.getLooper(), BorderLayout.CENTER);
        pack();
        setVisible(true);
    }
}