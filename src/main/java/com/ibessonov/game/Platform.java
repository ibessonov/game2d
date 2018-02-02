package com.ibessonov.game;

import com.ibessonov.game.core.common.Disposable;
import com.ibessonov.game.core.geometry.Rectangular;

/**
 * @author ibessonov
 */
public interface Platform extends Rectangular, Updatable, Disposable, Drawable, HasRectangularHitBox {

    default void add(Entity entity) {}
    default void remove(Entity entity) {}
}
