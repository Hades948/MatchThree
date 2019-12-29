package com.tylerroyer.matchthree;

import com.tylerroyer.molasses.*;

public class Driver {
    public static void main( String[] args ) {
        Config.windowWidth = 867;   Config.windowHeight = 910;
        Config.windowTitle = "Krystal's Quest";
        Config.firstScreen = new GridScreen();
        
        Game.start();
    }
}
