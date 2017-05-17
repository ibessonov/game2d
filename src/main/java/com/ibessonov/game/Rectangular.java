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
        if (other.x() >= x() + width()) return false;
        if (other.x() + other.width() <= x()) return false;

        if (other.y() >= y() + height()) return false;
        if (other.y() + other.height() <= y()) return false;

        return true;
    }
}
