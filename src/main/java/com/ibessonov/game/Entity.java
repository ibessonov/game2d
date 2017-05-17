package com.ibessonov.game;

import com.ibessonov.game.physics.SpeedX;

import static com.ibessonov.game.Constants.TILE;
import static com.ibessonov.game.Conversion.toTile;
import static java.lang.Integer.signum;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author ibessonov
 */
public abstract class Entity extends Rectangle implements InGameObject, Drawable, HasLifeLevel {

    public SpeedX speedX;
    public int speedY;

    public int jumpSpeed;

    private Platform stickyPlatform;
    private int platformDx;
    private boolean gravityDown = true;

    private int lifeLevel = initialLifeLevel();

    public Entity(int width, int height, int jumpSpeed, int initialSpeed, int maxSpeed) {
        super(width, height);

        int scale = 6;
        this.speedX = new SpeedX(initialSpeed * scale, 1, maxSpeed * scale, scale);
        this.jumpSpeed = jumpSpeed;
    }

    public boolean updateJump(Level level) {
        speedY = level.gravity().accelerate(speedY);

        int ySpeedScaled = level.gravity().scale(speedY);
        y += ySpeedScaled;

//        if (ySpeedScaled != 0) {
        if (handleCeilCollision(level)) {
            speedY = max(speedY, 0);
            return true;
        }
        if (handleFloorCollision(level)) {
            speedY = min(speedY, 0);
            return true;
        }
//        }
        return false;
    }

    public void updateRunSpeed(Level level, boolean moveLeft, boolean moveRight) {
        if (stickyPlatform != null) {
            if (stickyPlatform.disposed()) {
                stickyPlatform = null;
            } else {
                x = stickyPlatform.x + platformDx;
            }
        }
        speedX.update(moveLeft, moveRight);

        int xSpeedScaled = speedX.value();
        x += xSpeedScaled;
//        if (xSpeedScaled != 0) {
        handleLeftCollision(level);
        handleRightCollision(level);
//        }

        stickyPlatform = platformBelowMe(level);
        if (stickyPlatform != null) {
            platformDx = x - stickyPlatform.x;
        }
    }

    public boolean handleJump(Level level, boolean jump) {
        if (gravityDown != level.gravity().isDown()) {
            stickyPlatform = null;
        }
        gravityDown = level.gravity().isDown();
        if (stickyPlatform != null) {
            if (stickyPlatform.disposed()) {
                stickyPlatform = null;
            } else {
                if (level.gravity().isDown()) {
                    y = stickyPlatform.y - height;
                } else {
                    y = stickyPlatform.y + stickyPlatform.height;
                }
            }
        }
        if (jump && isOnSurface(level)) {
            speedY = level.gravity().directedSpeed(-jumpSpeed);
            stickyPlatform = null;
            return true;
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
    public void decreaseLifeLevel(int damage) {
        lifeLevel = max(0, lifeLevel - damage);
    }

    @Override
    public void increaseLifeLevel(int diff) {
        lifeLevel = min(initialLifeLevel(), lifeLevel + diff);
    }

    private Platform platformBelowMe(Level level) {
        int sign = level.gravity().isDown() ? 1 : -1;

        y += sign;
        for (Platform platform : level.platforms().list()) {
            if (signum(platform.y - y) == sign && intersects(platform)) {
                y -= sign;
                return platform;
            }
        }
        y -= sign;

        return null;
    }

    protected boolean handleCeilCollision(Level level) {
        boolean tilesCollision = horizontalTilesCollision(toTile(y), level);
        if (tilesCollision) {
            y = (toTile(y) + 1) * TILE;
        }
        for (Platform platform : level.platforms().list()) {
            if (platform.centerY() < centerY() && intersects(platform)) {
                y = platform.y + platform.height;
                if (!level.gravity().isDown()) {
                    stickyPlatform = platform;
                    platformDx = x - platform.x;
                }
                return true;
            }
        }
        return tilesCollision;
    }

    protected boolean handleFloorCollision(Level level) {
        boolean tilesCollision = horizontalTilesCollision(toTile(y + height - 1), level);
        if (tilesCollision) {
            y = toTile(y + height - 1) * TILE - height;
            // bouncing could be added here
        }
        for (Platform platform : level.platforms().list()) {
            if (platform.centerY() > centerY() && intersects(platform)) {
                y = platform.y - height;
                if (level.gravity().isDown()) {
                    stickyPlatform = platform;
                    platformDx = x - platform.x;
                }
                return true;
            }
        }
        return tilesCollision;
    }

    protected void handleLeftCollision(Level level) {
        boolean tilesCollision = verticalCollision(toTile(x), level);
        if (tilesCollision) {
            x = (toTile(x) + 1) * TILE;
            speedX.stop();
        }
        for (Platform platform : level.platforms().list()) {
            if (platform.centerX() < centerX() && intersects(platform)) {
                x = platform.x + platform.width;
            }
        }
    }

    protected void handleRightCollision(Level level) {
        boolean tilesCollision = verticalCollision(toTile(x + width - 1), level);
        if (tilesCollision) {
            x = toTile(x + width - 1) * TILE - width;
            speedX.stop();
        }
        for (Platform platform : level.platforms().list()) {
            if (platform.centerX() > centerX() && intersects(platform)) {
                x = platform.x - width;
            }
        }
    }

    protected boolean isOnSurface(Level level) {
        if (platformBelowMe(level) != null) return true;
        if (level.gravity().isDown()) {
            return horizontalTilesCollision(toTile(y + height), level);
        } else {
            return horizontalTilesCollision(toTile(y - 1), level);
        }
    }

    protected boolean horizontalTilesCollision(int i, Level level) {
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
