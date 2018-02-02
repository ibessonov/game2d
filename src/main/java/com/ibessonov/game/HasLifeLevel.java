package com.ibessonov.game;

import com.ibessonov.game.core.common.Disposable;

/**
 * @author ibessonov
 */
public interface HasLifeLevel extends Disposable {

    int initialLifeLevel();

    int currentLifeLevel();

    void decreaseLifeLevel(int damage);

    void increaseLifeLevel(int diff);

    default boolean isDead() {
        return currentLifeLevel() <= 0;
    }

    @Override
    default void dispose() {
        decreaseLifeLevel(currentLifeLevel());
    }

    @Override
    default boolean disposed() {
        return isDead();
    }
}
