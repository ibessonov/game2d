package com.ibessonov.game;

/**
 * @author ibessonov
 */
public interface Updatable {

    default void updateY(Level level, Keyboard keyboard) {}

    default void updateX(Level level, Keyboard keyboard) {}
}
