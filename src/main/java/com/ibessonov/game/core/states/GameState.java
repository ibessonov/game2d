package com.ibessonov.game.core.states;

import com.ibessonov.game.Keyboard;
import com.ibessonov.game.core.common.State;

import java.awt.image.BufferedImage;

/**
 * @author ibessonov
 */
public interface GameState extends State<GameState> {

    void update(Keyboard keyboard);

    void render(BufferedImage image);
}
