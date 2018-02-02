package com.ibessonov.game;

import com.ibessonov.game.core.common.Disposable;
import com.ibessonov.game.core.geometry.Rectangular;
import com.ibessonov.game.core.geometry.hitbox.HasHitBox;

/**
 * @author ibessonov
 */
public interface Bullet extends Rectangular, Updatable, Hazard, Disposable, Drawable, HasHitBox {

}
