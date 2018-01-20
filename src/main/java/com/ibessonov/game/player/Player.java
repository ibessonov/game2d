package com.ibessonov.game.player;

import com.ibessonov.game.*;
import com.ibessonov.game.core.common.State;

/**
 * @author ibessonov
 */
public interface Player extends Updatable, HasLifeLevel, Rectangular, Positionable, Drawable, State<Player> {

    Bullet fireBullet(Keyboard keyboard);
}
