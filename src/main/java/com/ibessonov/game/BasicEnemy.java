package com.ibessonov.game;

import java.awt.*;

import static com.ibessonov.game.Constants.TILE;

/**
 * @author ibessonov
 */
public class BasicEnemy extends Entity implements HasLifeLevel {

    private boolean moveRight = false;

    public BasicEnemy() {
        super(2 * TILE, TILE, 0, 3, 1);
    }

    public void update(Level level) {
        updateJump(level);
        updateRunSpeed(level, !moveRight, moveRight);
        if (speedX.value() == 0) {
            moveRight = !moveRight;
        }
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        g.setColor(Color.YELLOW);
        g.fillRect(x - xOffset, y - yOffset, width, height);

        g.setColor(Color.BLACK);
        g.drawRect(x - xOffset, y - yOffset, width - 1, height - 1);
    }
}
