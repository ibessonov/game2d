package com.ibessonov.game;

import java.awt.*;

import static com.ibessonov.game.Conversion.toTile;

/**
 * @author ibessonov
 */
public class SimpleBullet extends Entity implements Bullet {

    private boolean disposed = false;

    public SimpleBullet(float speedX, float speedY) {
        super(3, 3, 0);
        this.speedX.set(speedX);
        this.speedY.set(speedY);
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
    public void updateY(Level level, Keyboard keyboard) {
        y.add(speedY);
        float oldSpeedY = speedY.floatValue();
        if (!disposed && updateYCollision(level)) {
            int j = toTile(centerX());
            if (oldSpeedY > 0) {
                int i = toTile(y() + height);
                level.bulletHit(i, j, this);
            } else if (oldSpeedY < 0) {
                int i = toTile(y() - 1);
                level.bulletHit(i, j, this);
            }
            dispose();
        }
    }

    @Override
    public void updateX(Level level, Keyboard keyboard) {
        x.add(speedX);
        float oldSpeedX = speedX.floatValue();
        if (!disposed && updateXCollision(level)) {
            int i = toTile(centerY());
            if (oldSpeedX > 0) {
                int j = toTile(x() + width);
                level.bulletHit(i, j, this);
            } else if (oldSpeedX < 0) {
                int j = toTile(x() - 1);
                level.bulletHit(i, j, this);
            }
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
