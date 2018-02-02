package com.ibessonov.game.core.geometry;

/**
 * @author ibessonov
 */
public interface Rectangular extends Centered {

    int x();
    int y();
    int width();
    int height();

    default int centerX() {
        return x() + width() / 2;
    }

    default int centerY() {
        return y() + height() / 2;
    }
}
