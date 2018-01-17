package com.ibessonov.game.enemies;

import com.ibessonov.game.*;

import java.awt.*;

import static com.ibessonov.game.Constants.*;

/**
 * @author ibessonov
 */
public class BasicEnemy extends Entity implements HasLifeLevel, Hazard {

    public BasicEnemy() {
        super(2 * TILE - 8, TILE, 0, 1,1, 0.25f);
    }

    @Override
    public int damage() {
        return 1;
    }

    @Override
    public void updateY(Level level) {
        super.updateY(level);
        float temp = speedY.floatValue();
        if (updateGravityAndCollisions(level)) {
            speedY.set(temp * -0.5f); // bounce
            if (speedY.intValue() == 0) {
                speedY.set(0);
            }
        }
    }

    @Override
    public void updateX(Level level) {
        updateRunSpeed(level, !facingRight, facingRight);
        if (speedX.floatValue() == 0f) {
            facingRight ^= true;
        }
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        if (x() - xOffset <= -width || x() - xOffset >= SCREEN_WIDTH) {
            return;
        }
        if (y() - yOffset <= -height || y() - yOffset >= SCREEN_HEIGHT) {
            return;
        }
        new Sprite(0, 0, width, height, EnemiesSprites.BASIC_ENEMY_SPRITE)
                .draw(x() - xOffset, y() - yOffset, facingRight, facingDown, g);
    }
}
