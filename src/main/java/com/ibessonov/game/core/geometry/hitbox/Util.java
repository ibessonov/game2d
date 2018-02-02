package com.ibessonov.game.core.geometry.hitbox;

/**
 * @author ibessonov
 */
class Util {

    static boolean intersects(RectangularHitBox rect1, RectangularHitBox rect2) {
        return rect2.x() < rect1.x() + rect1.width()
            && rect2.y() < rect1.y() + rect1.height()
            && rect2.x() + rect2.width() > rect1.x()
            && rect2.y() + rect2.height() > rect1.y();
    }

    //TODO not tested
    static boolean intersects(RectangularHitBox rect, CircularHitBox circle) {
        int dx = Math.abs(circle.centerX() - rect.x());
        int dy = Math.abs(circle.centerY() - rect.y());

        if (dx > rect.width() / 2 + circle.radius()) return false;
        if (dy > rect.height() / 2 + circle.radius()) return false;

        if (dx <= rect.width() / 2) return true;
        if (dy <= rect.height() / 2) return true;

        int cd = (dx - rect.width() / 2) * (dx - rect.width() / 2) +
                 (dy - rect.height() / 2) * (dy - rect.height() / 2);

        return cd * cd <= circle.radius() * circle.radius();
    }

    static boolean intersects(CircularHitBox circle1, CircularHitBox circle2) {
        int dx = circle1.centerX() - circle2.centerX();
        int dy = circle1.centerY() - circle2.centerY();
        int r = circle1.radius() + circle2.radius();

        return dx * dx + dy * dy <= r * r;
    }
}
