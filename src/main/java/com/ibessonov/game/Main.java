package com.ibessonov.game;

import static com.ibessonov.game.Context.injector;

public class Main {

    public static void main(String args[]) {
        Game game = injector().getInstance(Game.class);
        Timer.run(game::tick, 60);
    }
}
