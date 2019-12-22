package com.tylerroyer.engine;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

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
        centerOnScreen();
        setVisible(true);
    }

    private void centerOnScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowX, windowY;

        windowX = (int) (screenSize.getWidth() / 2 - this.getWidth() / 2);
        windowY = (int) (screenSize.getHeight() / 2 - this.getHeight() / 2);

        setLocation(new Point(windowX, windowY));
    }
}