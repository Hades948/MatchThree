package com.tylerroyer.matchthree;

import javax.swing.JFrame;
import java.awt.BorderLayout;

public class Window extends JFrame {

    public Window() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Match Three");
        getContentPane().add(Game.getLooper(), BorderLayout.CENTER);
        pack();
        setVisible(true);
    }
}