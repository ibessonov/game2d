package com.ibessonov.game;

/**
 * @author ibessonov
 */
public interface Updatable {

    default void updateY(Level level) {}

    default void updateX(Level level) {}
}
