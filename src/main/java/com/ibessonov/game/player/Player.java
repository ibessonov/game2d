package com.ibessonov.game.player;

import com.ibessonov.game.*;

import static com.ibessonov.game.Context.inject;

/**
 * @author ibessonov
 */
public interface Player extends Updatable, HasLifeLevel, Rectangular, Positionable, Drawable {

    SimpleBullet fireBullet();

    Player next();

    static Player defaultPlayer() {
        return inject(new DefaultPlayer());
    }
}
