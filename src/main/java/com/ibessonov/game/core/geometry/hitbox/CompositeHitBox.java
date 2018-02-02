package com.ibessonov.game.core.geometry.hitbox;

import java.util.Collection;

/**
 * @author ibessonov
 */
public abstract class CompositeHitBox implements HitBox {

    @Override
    public boolean intersects(HitBox hitBox) {
        return this.hitBoxes().stream().anyMatch(hitBox::intersects);
    }

    @Override
    public final <T> T accept(HitBoxVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public abstract Collection<HitBox> hitBoxes();
}
