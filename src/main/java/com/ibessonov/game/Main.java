package com.ibessonov.game;

import com.ibessonov.game.player.BridgedPlayer;
import com.ibessonov.game.player.Player;

import static com.google.inject.Guice.createInjector;

public class Main {

    public static void main(String args[]) {
        createInjector(binder ->
            binder.bind(Player.class).to(BridgedPlayer.class)
        ).getInstance(MainCanvas.class);
    }
}
