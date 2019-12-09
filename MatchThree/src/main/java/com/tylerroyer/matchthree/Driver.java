package com.tylerroyer.matchthree;

import com.tylerroyer.engine.Game;

public class Driver {
    public static void main( String[] args ) {
        Game.setCurrentScreen(new GridScreen());
        Game.init();
    }
}
