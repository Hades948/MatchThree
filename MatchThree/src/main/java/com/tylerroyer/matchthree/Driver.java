package com.tylerroyer.matchthree;

public class Driver {
    public static void main( String[] args ) {
        Game.setCurrentScreen(new GridScreen());
        Game.init();
    }
}
