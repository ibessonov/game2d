package com.ibessonov.game;

import com.ibessonov.game.core.geometry.SubPixel;
import com.ibessonov.game.core.physics.SpeedInfo;
import com.ibessonov.game.util.BiIntPredicate;

import static com.ibessonov.game.Constants.TILE;
import static com.ibessonov.game.Conversion.toTile;
import static com.ibessonov.game.core.geometry.SubPixel.subPixel;
import static java.lang.Integer.signum;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author ibessonov
 */
public abstract class Entity implements Updatable, Drawable, HasLifeLevel, Rectangular, Positionable {

    protected final SubPixel x = subPixel(0);
    protected final SubPixel y = subPixel(0);

    protected final int width;
    protected final int height;

    protected final SubPixel speedX = subPixel(0);
    protected final SpeedInfo speedInfo;

    protected final SubPixel speedY = subPixel(0);
    protected final SubPixel jumpSpeed;

    private Platform currentPlatform;
    private final SubPixel currentPlatformDx = subPixel(0);

    protected boolean facingRight = true;
    protected boolean facingDown = true;

    private int lifeLevel = initialLifeLevel();

    public Entity(int width, int height, float jumpSpeed, float initialSpeed, float maxSpeed, float acceleration) {
        this.width = width;
        this.height = height;

        this.speedInfo = new SpeedInfo(initialSpeed, acceleration, maxSpeed);
        this.jumpSpeed = subPixel(jumpSpeed);
    }

    @Override
    public void setPosition(int x, int y) {
        this.x.set(x);
        this.y.set(y);
    }

    @Override
    public int x() {
        return x.intValue();
    }

    @Override
    public int y() {
        return y.intValue();
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
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
        updateGravity(level);
        return updateYCollision(level);
    }

    protected void updateGravity(Level level) {
        level.gravity().accelerate(speedY);
        y.add(speedY);
    }

    protected boolean updateYCollision(Level level) {
        if (handleCeilCollision(level)) {
            if (speedY.floatValue() < 0f) {
                speedY.set(0);
            }
            return true;
        }
        if (handleFloorCollision(level)) {
            if (speedY.floatValue() > 0f) {
                speedY.set(0);
            }
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
                x.set(currentPlatform.x());
                x.add(currentPlatformDx);
            }
        }
        speedInfo.update(speedX, moveLeft, moveRight);
        x.add(speedX);

        updateXCollision(level);
    }

    protected boolean updateXCollision(Level level) {
        boolean collision = handleLeftCollision(level);
        collision |= handleRightCollision(level);

        setCurrentPlatform(platformBelowMe(level));
        if (currentPlatform != null) {
            currentPlatformDx.set(x);
            currentPlatformDx.add(-currentPlatform.x());
        }
        return collision;
    }

    public boolean handleJump(Level level, boolean jump) {
        if (currentPlatform != null) {
            if (currentPlatform.disposed()) {
                clearCurrentPlatform();
            } else {
                if (facingDown) {
                    y.set(currentPlatform.y() - height);
                } else {
                    y.set(currentPlatform.y() + currentPlatform.height());
                }
            }
        }
        if (jump && isOnSurface(level)) {
            speedY.set(level.gravity().directedSpeed(-jumpSpeed.floatValue()));
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

        y.add(sign);
        for (Platform platform : level.platforms()) {
            if (signum(platform.y() - y()) == sign && intersects(platform)) {
                y.add(-sign);
                y.round();
                return platform;
            }
        }
        y.add(-sign);

        return null;
    }

    protected boolean handleCeilCollision(Level level) {
        boolean tilesCollision = horizontalTilesCollision(toTile(y()), level);
        if (tilesCollision && facingDown) {
            x.add(-1);
            tilesCollision = horizontalTilesCollision(toTile(y()), level);
            if (tilesCollision) {
                x.add(2);
                tilesCollision = horizontalTilesCollision(toTile(y()), level);
                if (tilesCollision) {
                    x.add(-1);
                } else {
                    x.round();
                }
            } else {
                x.round();
            }
        }
        if (tilesCollision) {
            y.set((toTile(y()) + 1) * TILE);
        }
        for (Platform platform : level.platforms()) {
            if (platform.centerY() < centerY() && intersects(platform)) {
                y.set(platform.y() + platform.height());
                if (!facingDown) {
                    setCurrentPlatform(platform);
                    currentPlatformDx.set(x);
                    currentPlatformDx.add(-platform.x());
                }
                return true;
            }
        }
        return tilesCollision;
    }

    protected boolean handleFloorCollision(Level level) {
        boolean tilesCollision = horizontalTilesCollision(toTile(y() + height - 1), level);
        if (tilesCollision && !facingDown) {
            x.add(-1);
            tilesCollision = horizontalTilesCollision(toTile(y() + height - 1), level);
            if (tilesCollision) {
                x.add(2);
                tilesCollision = horizontalTilesCollision(toTile(y() + height - 1), level);
                if (tilesCollision) {
                    x.add(-1);
                } else {
                    x.round();
                }
            } else {
                x.round();
            }
        }
        if (tilesCollision) {
            y.set(toTile(y() + height - 1) * TILE - height);
            // bouncing could be added here
        }
        for (Platform platform : level.platforms()) {
            if (platform.centerY() > centerY() && intersects(platform)) {
                y.set(platform.y() - height);
                if (facingDown) {
                    currentPlatformDx.set(x);
                    currentPlatformDx.add(-platform.x());
                }
                return true;
            }
        }
        return tilesCollision;
    }

    protected boolean handleLeftCollision(Level level) {
        boolean tilesCollision = verticalCollision(toTile(x()), level);
        if (tilesCollision) {
            x.set((toTile(x()) + 1) * TILE);
            speedX.set(0);
        }
        for (Platform platform : level.platforms()) {
            if (platform.centerX() < centerX() && intersects(platform)) {
                x.set(platform.x() + platform.width());
                speedX.set(0);
                return true;
            }
        }
        return tilesCollision;
    }

    protected boolean handleRightCollision(Level level) {
        boolean tilesCollision = verticalCollision(toTile(x() + width - 1), level);
        if (tilesCollision) {
            x.set(toTile(x() + width - 1) * TILE - width);
            speedX.set(0);
        }
        for (Platform platform : level.platforms()) {
            if (platform.centerX() > centerX() && intersects(platform)) {
                x.set(platform.x() - width);
                speedX.set(0);
                return true;
            }
        }
        return tilesCollision;
    }

    protected boolean isOnSurface(Level level) {
        if (platformBelowMe(level) != null) return true;
        if (facingDown) {
            return horizontalTilesCollision(toTile(y() + height), level);
        } else {
            return horizontalTilesCollision(toTile(y() - 1), level);
        }
    }

    protected boolean horizontalTilesCollision(int i, Level level) {
        for (int j = toTile(x()), jRight = toTile(x() + width - 1); j <= jRight; j++) {
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
        for (int i = toTile(y()), iFloor = toTile(y() + height - 1); i <= iFloor; i++) {
            if (condition.test(i, j)) {
                return true;
            }
        }
        return false;
    }
}
