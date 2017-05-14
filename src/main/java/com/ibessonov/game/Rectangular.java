package com.ibessonov.game;

/**
 * @author ibessonov
 */
public interface Rectangular {

    int x();
    int y();
    int width();
    int height();

    default boolean intersects(Rectangular other) {
        if (other.x() >= x() + width()) return false;
        if (other.x() + other.width() <= x()) return false;

        if (other.y() >= y() + height()) return false;
        if (other.y() + other.height() <= y()) return false;

        return true;
    }
}
