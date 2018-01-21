package com.ibessonov.game;

import com.ibessonov.game.core.states.GameState;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import static com.ibessonov.game.Effects.blur;

public class PauseGameState implements GameState {

    private final GameState previous;
    private boolean unpause = false;
    private int duration = 0;

    public PauseGameState(GameState previous) {
        this.previous = previous;
    }

    @Override
    public void update(Keyboard keyboard) {
        unpause = keyboard.isStartTapped();
    }

    @Override
    public void render(BufferedImage image) {
        if (++duration > 20) return;
        int[][] matrix = {
                {0, 1, 0},
                {1, 252, 1},
                {0, 1, 0}
        };
        blur(((DataBufferInt) image.getRaster().getDataBuffer()).getData(), matrix);
    }

    @Override
    public GameState next() {
        return unpause ? previous : this;
    }
}
