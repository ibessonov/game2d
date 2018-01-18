package com.ibessonov.game;

import java.awt.*;

import static com.ibessonov.game.Conversion.toTile;
import static java.lang.Math.abs;

/**
 * @author ibessonov
 */
public class SimpleBullet extends Entity implements Bullet {

    private boolean disposed = false;

    public SimpleBullet(int speedX, int speedY) {
        super(size(speedX, speedY), size(speedY, speedX), 0, abs(speedX), abs(speedX), 0f);
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
        float oldSpeedY = speedY.floatValue();
        if (!disposed && updateYCollision(level)) {
            int j = toTile(centerX());
            if (oldSpeedY > 0) {
                int i = toTile(y() + height);
//                if (level.isSolid(i - 1, j)) {
//                    i--;
//                }
                level.bulletHit(i, j, this);
            } else if (oldSpeedY < 0) {
                int i = toTile(y() - 1);
//                if (level.isSolid(i + 1, j)) {
//                    i++;
//                }
                level.bulletHit(i, j, this);
            }
            dispose();
        }
    }

    @Override
    public void updateX(Level level) {
        x.add(speedX);
        float oldSpeedX = speedX.floatValue();
        if (!disposed && updateXCollision(level)) {
            int i = toTile(centerY());
            if (oldSpeedX > 0) {
                int j = toTile(x() + width);
//                if (level.isSolid(i, j - 1)) {
//                    j--;
//                }
                level.bulletHit(i, j, this);
            } else if (oldSpeedX < 0) {
                int j = toTile(x() - 1);
//                if (level.isSolid(i, j + 1)) {
//                    j++;
//                }
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
