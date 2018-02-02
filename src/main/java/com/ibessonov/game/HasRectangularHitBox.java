package com.ibessonov.game;

import com.ibessonov.game.core.geometry.Rectangular;
import com.ibessonov.game.core.geometry.hitbox.HasHitBox;
import com.ibessonov.game.core.geometry.hitbox.HitBox;
import com.ibessonov.game.core.geometry.hitbox.RectangularHitBox;

// temporary
public interface HasRectangularHitBox extends Rectangular, HasHitBox {

    @Override
    default HitBox hitBox() {
        return new RectangularHitBox() {
            @Override
            public int x() {
                return HasRectangularHitBox.this.x();
            }

            @Override
            public int y() {
                return HasRectangularHitBox.this.y();
            }

            @Override
            public int width() {
                return HasRectangularHitBox.this.width();
            }

            @Override
            public int height() {
                return HasRectangularHitBox.this.height();
            }
        };
    }
}
