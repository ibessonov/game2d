package com.ibessonov.game;

/**
 * @author ibessonov
 */
public interface HasLifeLevel {

    int initialLifeLevel();

    int currentLifeLevel();

    int decreaseLifeLevel(int damage);

    default boolean isDead() {
        return currentLifeLevel() <= 0;
    }
}
