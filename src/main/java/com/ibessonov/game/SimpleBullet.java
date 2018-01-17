package com.ibessonov.game;

import java.awt.*;

import static java.lang.Math.abs;

/**
 * @author ibessonov
 */
public class SimpleBullet extends Entity implements Bullet {

    private boolean disposed = false;

    public SimpleBullet(int speedX, int speedY) {
        super(size(speedX, speedY), size(speedY, speedX), 0, abs(speedX), abs(speedX), 0.25f);
        this.speedInfo.update(this.speedX, speedX < 0, speedX > 0);
        this.speedY.set(speedY);
    }

    private static int size(int speedX, int speedY) {
        if (speedX == 0) return 3;
        if (speedY == 0) return 5;
        return 4;
    }

    @Override
    public int damage() {
        return 5;
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        g.setColor(Color.WHITE);
        g.fillRect(x() - xOffset, y() - yOffset, width, height);

        g.setColor(Color.RED);
        g.drawRect(x() - xOffset, y() - yOffset, width - 1, height - 1);
    }

    @Override
    public void updateY(Level level) {
        y.add(speedY);
        if (updateYCollision(level) && !disposed) {
            dispose();
        }
    }

    @Override
    public void updateX(Level level) {
        x.add(speedX);
        if (updateXCollision(level) && !disposed) {
            dispose();
        }
    }

    @Override
    public void dispose() {
        disposed = true;
    }

    @Override
    public boolean disposed() {
        return disposed;
    }
}
