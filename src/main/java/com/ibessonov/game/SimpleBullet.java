package com.ibessonov.game;

import java.awt.*;

/**
 * @author ibessonov
 */
public class SimpleBullet extends Entity implements Bullet {

    private boolean disposed = false;

    public SimpleBullet(int x, int y, boolean facingRight) {
        super(5, 3, 0, 6, 6);
        setPosition(x, y);
        speedX.update(!facingRight, facingRight);
    }

    @Override
    public int damage() {
        return 5;
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        g.setColor(Color.WHITE);
        g.fillRect(x - xOffset, y - yOffset, width, height);

        g.setColor(Color.RED);
        g.drawRect(x - xOffset, y - yOffset, width - 1, height - 1);
    }

    @Override
    public void updateY(Level level) {

    }

    @Override
    public void updateX(Level level) {
        updateRunSpeed(level, speedX.value() < 0, speedX.value() > 0);
        if (speedX.value() == 0 && !disposed) {
            dispose();
        }
    }

    @Override
    public void dispose() {
        disposed = true;
        x = Integer.MIN_VALUE;
        y = Integer.MIN_VALUE;
    }

    @Override
    public boolean disposed() {
        return disposed;
    }
}
