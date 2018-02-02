package com.ibessonov.game.core.geometry.hitbox;

/**
 * @author ibessonov
 */
public interface HitBox {

    boolean intersects(HitBox hitBox);

    <T> T accept(HitBoxVisitor<T> visitor);


    interface HitBoxVisitor<T> {

        T visit(RectangularHitBox hitBox);

        T visit(CircularHitBox hitBox);

        T visit(CompositeHitBox hitBox);
    }
}
