package com.ibessonov.game;

import com.ibessonov.game.physics.SpeedX;

import static com.ibessonov.game.Constants.TILE;
import static com.ibessonov.game.Conversion.toTile;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author ibessonov
 */
public abstract class Entity extends Rectangle implements Drawable, HasLifeLevel {

    public SpeedX speedX;
    public int speedY;

    public int jumpSpeed;

    private int lifeLevel = initialLifeLevel();

    public Entity(int width, int height, int jumpSpeed, int initialSpeed, int maxSpeed) {
        super(width, height);

        int scale = 3;
        this.speedX = new SpeedX(initialSpeed * scale, 1, maxSpeed * scale, scale);
        this.jumpSpeed = jumpSpeed;
    }

    public void updateRunSpeed(Level level, boolean moveLeft, boolean moveRight) {
        speedX.update(moveLeft, moveRight);

        int xSpeedScaled = speedX.value();
        x += xSpeedScaled;
        if (xSpeedScaled != 0) {
            if (leftCollision(level)) {
                int j = toTile(x);
                x = (j + 1) * TILE;
                speedX.stop();
            }
            if (rightCollision(level)) {
                int j = toTile(x + width - 1);
                x = j * TILE - width;
                speedX.stop();
            }
        }
    }

    public boolean handleJump(Level level, boolean jump) {
        if (jump && isOnSurface(level)) {
            speedY = level.gravity().directedSpeed(-jumpSpeed);
            return true;
        }
        return false;
    }

    public boolean updateJump(Level level) {
        speedY = level.gravity().accelerate(speedY);

        int ySpeedScaled = level.gravity().scale(speedY);
        y += ySpeedScaled;

        if (ySpeedScaled != 0) {
            if (ceilCollision(level)) {
                int i = toTile(y);
                y = (i + 1) * TILE;
                speedY = 0;
                return true;
            }
            if (floorCollision(level)) {
                int i = toTile(y + height - 1);
                y = i * TILE - height;
                speedY = 0; // bouncing could be added here
                return true;
            }
        }
        return false;
    }

    @Override
    public int initialLifeLevel() {
        return 10;
    }

    @Override
    public int currentLifeLevel() {
        return lifeLevel;
    }

    @Override
    public int decreaseLifeLevel(int damage) {
        return lifeLevel -= damage;
    }

    protected boolean ceilCollision(Level level) {
        return horizontalCollision(toTile(y), level);
    }

    protected boolean floorCollision(Level level) {
        return horizontalCollision(toTile(y + height - 1), level);
    }

    protected boolean leftCollision(Level level) {
        return verticalCollision(toTile(x), level);
    }

    protected boolean rightCollision(Level level) {
        return verticalCollision(toTile(x + width - 1), level);
    }

    protected boolean isOnSurface(Level level) {
        if (level.gravity().isDown()) {
            return horizontalCollision(toTile(y + height), level);
        } else {
            return horizontalCollision(toTile(y - 1), level);
        }
    }

    protected boolean horizontalCollision(int i, Level level) {
        for (int j = toTile(x), jRight = toTile(x + width - 1); j <= jRight; j++) {
            if (level.isSolid(i, j)) {
                return true;
            }
        }
        return false;
    }

    protected boolean verticalCollision(int j, Level level) {
        for (int i = toTile(y), iFloor = toTile(y + height - 1); i <= iFloor; i++) {
            if (level.isSolid(i, j)) {
                return true;
            }
        }
        return false;
    }
}
