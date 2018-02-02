package com.ibessonov.game.core.geometry.hitbox;

import com.ibessonov.game.core.geometry.Rectangular;

/**
 * @author ibessonov
 */
public abstract class RectangularHitBox implements HitBox, Rectangular {

    @Override
    public boolean intersects(HitBox hitBox) {
        return hitBox.accept(new HitBoxVisitor<>() {
            @Override
            public Boolean visit(RectangularHitBox hitBox) {
                return Util.intersects(RectangularHitBox.this, hitBox);
            }

            @Override
            public Boolean visit(CircularHitBox hitBox) {
                return Util.intersects(RectangularHitBox.this, hitBox);
            }

            @Override
            public Boolean visit(CompositeHitBox hitBox) {
                return hitBox.hitBoxes().stream().anyMatch(RectangularHitBox.this::intersects);
            }
        });
    }

    @Override
    public final <T> T accept(HitBoxVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
