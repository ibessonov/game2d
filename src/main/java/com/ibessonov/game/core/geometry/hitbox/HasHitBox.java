package com.ibessonov.game.core.geometry.hitbox;

/**
 * @author ibessonov
 */
public interface HasHitBox {

    HitBox hitBox();

    default boolean intersects(HasHitBox other) {
        return hitBox().intersects(other.hitBox());
    }
}
