package com.ibessonov.game.enemies;

import com.ibessonov.game.*;
import com.ibessonov.game.core.physics.SpeedInfo;

import java.awt.*;

import static com.ibessonov.game.Constants.*;
import static com.ibessonov.game.core.physics.XDirection.LEFT;
import static com.ibessonov.game.core.physics.XDirection.RIGHT;

/**
 * @author ibessonov
 */
public class BasicEnemy extends Entity implements HasLifeLevel, Hazard {

    private SpeedInfo speedInfo = new SpeedInfo(1f, .25f, 1f);

    public BasicEnemy() {
        super(2 * TILE - 8, TILE, 0);
    }

    @Override
    public int damage() {
        return 1;
    }

    @Override
    public void updateY(Level level, Keyboard keyboard) {
        super.updateY(level, keyboard);
        float oldSpeedY = speedY.floatValue();
        updateGravity(level);
        if (updateYCollision(level)) {
            speedY.set(oldSpeedY * -0.5f); // bounce
            if (speedY.intValue() == 0) {
                speedY.set(0);
            }
        }
    }

    @Override
    public void updateX(Level level, Keyboard keyboard) {
        updateRunSpeed(level, keyboard, xDirection == LEFT, xDirection == RIGHT);
        if (speedX.floatValue() == 0f) {
            xDirection = xDirection == RIGHT ? LEFT : RIGHT;
        }
    }

    @Override
    protected void accelerate(Keyboard keyboard, boolean moveLeft, boolean moveRight) {
        speedInfo.update(speedX, moveLeft, moveRight);
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
                .draw(x() - xOffset, y() - yOffset, xDirection, yDirection, g);
    }
}
