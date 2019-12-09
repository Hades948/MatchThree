package com.tylerroyer.engine;

import javax.swing.JFrame;
import java.awt.BorderLayout;

public class Window extends JFrame {
    private static final long serialVersionUID = 4159603562974028158L;

    Window() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Match Three");
        getContentPane().add(Game.getLooper(), BorderLayout.CENTER);
        pack();
        setVisible(true);
    }
}