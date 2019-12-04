package com.tylerroyer.matchthree;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.*;

public class Window extends JFrame {

    public Window() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Match Three");
        getContentPane().add(Game.getRenderer(), BorderLayout.CENTER);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { Game.stop(); }});

        pack();
        setVisible(true);
    }
}