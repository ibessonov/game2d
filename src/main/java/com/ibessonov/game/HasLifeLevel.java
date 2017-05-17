package com.ibessonov.game;

/**
 * @author ibessonov
 */
public interface HasLifeLevel {

    int initialLifeLevel();

    int currentLifeLevel();

    void decreaseLifeLevel(int damage);

    void increaseLifeLevel(int diff);

    default boolean isDead() {
        return currentLifeLevel() <= 0;
    }
}
