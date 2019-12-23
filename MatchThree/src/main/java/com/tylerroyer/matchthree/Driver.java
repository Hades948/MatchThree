package com.tylerroyer.matchthree;

import com.tylerroyer.molasses.*;

public class Driver {
    public static void main( String[] args ) {
        Resources.init();
        Game.init(867, 910, "Krystal's Quest", new GridScreen());
    }
}
