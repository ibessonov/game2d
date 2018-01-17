package com.ibessonov.game.player;

import com.ibessonov.game.*;

/**
 * @author ibessonov
 */
public interface Player extends Updatable, HasLifeLevel, Rectangular, Positionable, Drawable {

    SimpleBullet fireBullet();

    Player next();
}
