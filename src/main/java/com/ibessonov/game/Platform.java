package com.ibessonov.game;

/**
 * @author ibessonov
 */
public interface Platform extends Rectangular, Updatable, Disposable, Drawable {

    default void add(Entity entity) {}
    default void remove(Entity entity) {}
}
