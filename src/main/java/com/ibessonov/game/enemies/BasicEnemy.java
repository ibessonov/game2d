package com.ibessonov.game.enemies;

import com.ibessonov.game.*;

import java.awt.*;

import static com.ibessonov.game.Constants.TILE;

/**
 * @author ibessonov
 */
public class BasicEnemy extends Entity implements HasLifeLevel, Hazard {

    private boolean facingRight = false;
    private boolean facingDown = true;

    public BasicEnemy() {
        super(2 * TILE - 8, TILE, 0, 3, 1);
    }

    @Override
    public int damage() {
        return 1;
    }

    @Override
    public void updateY(Level level) {
        facingDown = level.gravity().isDown();
        updateJump(level);
    }

    @Override
    public void updateX(Level level) {
        updateRunSpeed(level, !facingRight, facingRight);
        if (speedX.value() == 0) {
            facingRight ^= true;
        }
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        new Sprite(0, 0, width, height, EnemiesSprites.BASIC_ENEMY_SPRITE)
                .draw(x - xOffset, y - yOffset, facingRight, facingDown, g);
    }
}
