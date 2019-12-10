package com.tylerroyer.matchthree;

import com.tylerroyer.engine.*;

public class Driver {
    public static void main( String[] args ) {
        Resources.init();

        Game.setCurrentScreen(new GridScreen());
        Game.init(867, 910, "Crystal's Quest");
    }
}
