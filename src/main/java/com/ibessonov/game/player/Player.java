package com.ibessonov.game.player;

import com.ibessonov.game.*;

/**
 * @author ibessonov
 */
public interface Player extends HasLifeLevel, Rectangular, Positionable, Drawable {

    void update(Level level);

    Bullet fireBullet();

    Player next();

    static Player defaultPlayer() {
        return new DefaultPlayer();
    }
}
