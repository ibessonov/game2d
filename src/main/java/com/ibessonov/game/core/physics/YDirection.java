package com.ibessonov.game.core.physics;

/**
 * @author ibessonov
 */
public enum YDirection {

    UP(-1), DOWN(1);

    public final int sign;

    YDirection(int sign) {
        this.sign = sign;
    }
}
