package com.ibessonov.game;

import static com.ibessonov.game.Context.injector;

public class Main {

    public static void main(String args[]) {
        injector().getInstance(Game.class).start();
    }
}
