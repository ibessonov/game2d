package com.ibessonov.game;

import com.ibessonov.game.physics.SpeedX;
import com.ibessonov.game.util.BiIntPredicate;

import static com.ibessonov.game.Constants.TILE;
import static com.ibessonov.game.Conversion.toTile;
import static java.lang.Integer.signum;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author ibessonov
 */
public abstract class Entity extends Rectangle implements Updatable, Drawable, HasLifeLevel {

    protected SpeedX speedX;
    protected int speedY;

    protected int jumpSpeed;

    private Platform currentPlatform;
    private int currentPlatformDx;

    protected boolean facingRight = true;
    protected boolean facingDown = true;

    private int lifeLevel = initialLifeLevel();

    public Entity(int width, int height, int jumpSpeed, int initialSpeed, int maxSpeed) {
        super(width, height);

        int scale = 6;
        this.speedX = new SpeedX(initialSpeed * scale, 1, maxSpeed * scale, scale);
        this.jumpSpeed = jumpSpeed;
    }

    public void setOrientation(boolean facingRight, boolean facingDown) {
        this.facingRight = facingRight;
        this.facingDown = facingDown;
    }

    @Override
    public void updateY(Level level) {
        if (facingDown != level.gravity().isDown()) {
            clearCurrentPlatform();
        }
        facingDown = level.gravity().isDown();
    }

    public boolean updateGravityAndCollisions(Level level) {
        speedY = level.gravity().accelerate(speedY);

        y += level.gravity().scale(speedY);

        if (handleCeilCollision(level)) {
            speedY = max(speedY, 0);
            return true;
        }
        if (handleFloorCollision(level)) {
            speedY = min(speedY, 0);
            return true;
        }
        return false;
    }

    protected void updateHorizontalFacing(boolean moveLeft, boolean moveRight) {
        facingRight = moveRight || facingRight && !moveLeft;
    }

    public void updateRunSpeed(Level level, boolean moveLeft, boolean moveRight) {
        updateHorizontalFacing(moveLeft, moveRight);

        if (currentPlatform != null) {
            if (currentPlatform.disposed()) {
                clearCurrentPlatform();
            } else {
                x = currentPlatform.x() + currentPlatformDx;
            }
        }
        speedX.update(moveLeft, moveRight);

        x += speedX.value();
        handleLeftCollision(level);
        handleRightCollision(level);

        setCurrentPlatform(platformBelowMe(level));
        if (currentPlatform != null) {
            currentPlatformDx = x - currentPlatform.x();
        }
    }

    public boolean handleJump(Level level, boolean jump) {
        if (currentPlatform != null) {
            if (currentPlatform.disposed()) {
                clearCurrentPlatform();
            } else {
                if (facingDown) {
                    y = currentPlatform.y() - height;
                } else {
                    y = currentPlatform.y() + currentPlatform.height();
                }
            }
        }
        if (jump && isOnSurface(level)) {
            speedY = level.gravity().directedSpeed(-jumpSpeed);
            clearCurrentPlatform();
            return true;
        }
        return false;
    }

    private void setCurrentPlatform(Platform platform) {
        clearCurrentPlatform();
        if (platform != null) {
            platform.add(this);
            currentPlatform = platform;
        }
    }

    private void clearCurrentPlatform() {
        if (currentPlatform != null) {
            currentPlatform.remove(this);
            currentPlatform = null;
        }
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
        int sign = facingDown ? 1 : -1;

        y += sign;
        for (Platform platform : level.platforms().list()) {
            if (signum(platform.y() - y) == sign && intersects(platform)) {
                y -= sign;
                return platform;
            }
        }
        y -= sign;

        return null;
    }

    protected boolean handleCeilCollision(Level level) {
        boolean tilesCollision = horizontalTilesCollision(toTile(y), level);
        if (tilesCollision && facingDown) {
            x--;
            tilesCollision = horizontalTilesCollision(toTile(y), level);
            if (tilesCollision) {
                x += 2;
                tilesCollision = horizontalTilesCollision(toTile(y), level);
                if (tilesCollision) {
                    x--;
                }
            }
        }
        if (tilesCollision) {
            y = (toTile(y) + 1) * TILE;
        }
        for (Platform platform : level.platforms().list()) {
            if (platform.centerY() < centerY() && intersects(platform)) {
                y = platform.y() + platform.height();
                if (!facingDown) {
                    setCurrentPlatform(platform);
                    currentPlatformDx = x - platform.x();
                }
                return true;
            }
        }
        return tilesCollision;
    }

    protected boolean handleFloorCollision(Level level) {
        boolean tilesCollision = horizontalTilesCollision(toTile(y + height - 1), level);
        if (tilesCollision && !facingDown) {
            x--;
            tilesCollision = horizontalTilesCollision(toTile(y + height - 1), level);
            if (tilesCollision) {
                x += 2;
                tilesCollision = horizontalTilesCollision(toTile(y + height - 1), level);
                if (tilesCollision) {
                    x--;
                }
            }
        }
        if (tilesCollision) {
            y = toTile(y + height - 1) * TILE - height;
            // bouncing could be added here
        }
        for (Platform platform : level.platforms().list()) {
            if (platform.centerY() > centerY() && intersects(platform)) {
                y = platform.y() - height;
                if (facingDown) {
                    setCurrentPlatform(platform);
                    currentPlatformDx = x - platform.x();
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
                x = platform.x() + platform.width();
                speedX.stop();
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
                x = platform.x() - width;
                speedX.stop();
            }
        }
    }

    protected boolean isOnSurface(Level level) {
        if (platformBelowMe(level) != null) return true;
        if (facingDown) {
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
        return verticalCollision(j, level::isSolid);
    }

    protected boolean verticalCollision(int j, BiIntPredicate condition) {
        for (int i = toTile(y), iFloor = toTile(y + height - 1); i <= iFloor; i++) {
            if (condition.test(i, j)) {
                return true;
            }
        }
        return false;
    }
}
