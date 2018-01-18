package com.ibessonov.game;

import com.ibessonov.game.core.states.GameState;

import java.awt.image.BufferedImage;

public class PauseGameState implements GameState {

    private final GameState previous;
    private boolean unpause = false;

    public PauseGameState(GameState previous) {
        this.previous = previous;
    }

    @Override
    public void update(Keyboard keyboard) {
        unpause = keyboard.isStartTapped();
    }

    @Override
    public void render(BufferedImage image) {
    }

    @Override
    public GameState next() {
        return unpause ? previous : this;
    }
}
