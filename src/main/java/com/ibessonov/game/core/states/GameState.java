package com.ibessonov.game.core.states;

import com.ibessonov.game.Keyboard;

import java.awt.image.BufferedImage;

/**
 * @author ibessonov
 */
public interface GameState {

    void update(Keyboard keyboard);

    void render(BufferedImage image);

    GameState next();
}
