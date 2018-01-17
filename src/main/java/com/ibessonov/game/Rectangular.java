package com.ibessonov.game;

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

    default boolean intersects(Rectangular other) {
        return other.x() < x() + width()
            && other.y() < y() + height()
            && other.x() + other.width() > x()
            && other.y() + other.height() > y();
    }
}
