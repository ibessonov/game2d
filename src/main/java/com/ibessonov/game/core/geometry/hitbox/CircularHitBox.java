package com.ibessonov.game.core.geometry.hitbox;

import com.ibessonov.game.core.geometry.Centered;

/**
 * @author ibessonov
 */
public abstract class CircularHitBox implements HitBox, Centered {

    @Override
    public boolean intersects(HitBox hitBox) {
        return hitBox.accept(new HitBoxVisitor<>() {
            @Override
            public Boolean visit(RectangularHitBox hitBox) {
                return Util.intersects(hitBox, CircularHitBox.this);
            }

            @Override
            public Boolean visit(CircularHitBox hitBox) {
                return Util.intersects(CircularHitBox.this, hitBox);
            }

            @Override
            public Boolean visit(CompositeHitBox hitBox) {
                return hitBox.hitBoxes().stream().anyMatch(CircularHitBox.this::intersects);
            }
        });
    }

    @Override
    public final <T> T accept(HitBoxVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public abstract int radius();
}
