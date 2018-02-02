package com.ibessonov.game.player;

import com.ibessonov.game.*;
import com.ibessonov.game.core.common.State;
import com.ibessonov.game.core.geometry.Rectangular;
import com.ibessonov.game.core.geometry.hitbox.HasHitBox;

/**
 * @author ibessonov
 */
public interface Player extends Updatable, HasLifeLevel, Rectangular, Positionable, Drawable, HasHitBox, State<Player> {

    Bullet fireBullet(Keyboard keyboard);
}
