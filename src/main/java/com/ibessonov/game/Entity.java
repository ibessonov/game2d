package com.ibessonov.game;

import com.ibessonov.game.core.common.util.BiIntPredicate;
import com.ibessonov.game.core.geometry.Rectangular;
import com.ibessonov.game.core.geometry.SubPixel;
import com.ibessonov.game.core.physics.XDirection;
import com.ibessonov.game.core.physics.YDirection;

import static com.ibessonov.game.Constants.TILE;
import static com.ibessonov.game.Conversion.toTile;
import static com.ibessonov.game.core.geometry.SubPixel.subPixel;
import static com.ibessonov.game.core.physics.XDirection.LEFT;
import static com.ibessonov.game.core.physics.XDirection.RIGHT;
import static com.ibessonov.game.core.physics.YDirection.DOWN;
import static com.ibessonov.game.core.physics.YDirection.UP;
import static java.lang.Integer.signum;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author ibessonov
 */
//TODO rename and split into several classes
//TODO facing* fields don't feel like they belong here
public abstract class Entity implements Updatable, Drawable, HasLifeLevel, Rectangular, Positionable, HasRectangularHitBox {

    protected final SubPixel x = subPixel(0);
    protected final SubPixel y = subPixel(0);

    protected final int width;
    protected final int height;

    protected final SubPixel speedX = subPixel(0);
    protected final SubPixel speedY = subPixel(0);
    protected final SubPixel jumpSpeed;

    private Platform currentPlatform;
    private final SubPixel currentPlatformDx = subPixel(0);

    protected XDirection xDirection = RIGHT;
    protected YDirection yDirection = DOWN;

    private int lifeLevel = initialLifeLevel();

    public Entity(int width, int height, float jumpSpeed) {
        this.width = width;
        this.height = height;
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

    public void setOrientation(XDirection xDirection, YDirection yDirection) {
        this.xDirection = xDirection;
        this.yDirection = yDirection;
    }

    @Override
    public void updateY(Level level, Keyboard keyboard) {
        if (yDirection != level.gravity().yDirection()) {
            clearCurrentPlatform();
            yDirection = level.gravity().yDirection();
        }
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
        if (xDirection == RIGHT) {
            if (moveLeft) {
                xDirection = LEFT;
            }
        } else {
            if (moveRight) {
                xDirection = RIGHT;
            }
        }
    }

    public void updateRunSpeed(Level level, Keyboard keyboard, boolean moveLeft, boolean moveRight) {
        updateHorizontalFacing(moveLeft, moveRight);

        if (currentPlatform != null) {
            if (currentPlatform.disposed()) {
                clearCurrentPlatform();
            } else {
                x.set(currentPlatform.x());
                x.add(currentPlatformDx);
            }
        }
        accelerate(keyboard, moveLeft, moveRight);
        x.add(speedX);

        updateXCollision(level);
    }

    protected void accelerate(Keyboard keyboard, boolean moveLeft, boolean moveRight) {
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
                if (yDirection == DOWN) {
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
        int sign = yDirection.sign;

        y.add(sign);
        for (Platform platform : level.platforms()) {
            if (signum(platform.centerY() - centerY()) == sign && intersects(platform)) {
                y.add(-sign);
                y.round();
                return platform;
            }
        }
        y.add(-sign);

        return null;
    }

    protected boolean handleCeilCollision(Level level) {
        boolean tilesCollision = speedY.floatValue() < 0 && horizontalTilesCollision(toTile(y()), level);
        if (tilesCollision && yDirection == DOWN) {
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
                if (yDirection == UP) {
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
        boolean tilesCollision = speedY.floatValue() > 0 && horizontalTilesCollision(toTile(y() + height - 1), level);
        if (tilesCollision && yDirection == UP) {
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
                if (yDirection == DOWN) {
                    currentPlatformDx.set(x);
                    currentPlatformDx.add(-platform.x());
                }
                return true;
            }
        }
        return tilesCollision;
    }

    protected boolean handleLeftCollision(Level level) {
        boolean tilesCollision = speedX.floatValue() < 0 && verticalCollision(toTile(x()), level);
        if (tilesCollision) {
            x.set((toTile(x()) + 1) * TILE);
            speedX.set(0);
        }
        for (Platform platform : level.platforms()) {
            if (platform.centerX() < centerX() && intersects(platform)) {
                x.set(platform.x() + platform.width());
//                if (speedX.floatValue() < 0) {
//                    speedX.set(0);
//                }
                return true;
            }
        }
        return tilesCollision;
    }

    protected boolean handleRightCollision(Level level) {
        boolean tilesCollision = speedX.floatValue() > 0 && verticalCollision(toTile(x() + width - 1), level);
        if (tilesCollision) {
            x.set(toTile(x() + width - 1) * TILE - width);
            speedX.set(0);
        }
        for (Platform platform : level.platforms()) {
            if (platform.centerX() > centerX() && intersects(platform)) {
                x.set(platform.x() - width);
//                if (speedX.floatValue() > 0) {
//                    speedX.set(0);
//                }
                return true;
            }
        }
        return tilesCollision;
    }

    protected boolean isOnSurface(Level level) {
        if (platformBelowMe(level) != null) return true;
        if (yDirection == DOWN) {
            return horizontalTilesCollision(toTile(y() + height), level);
        } else {
            return horizontalTilesCollision(toTile(y() - 1), level);
        }
    }

    protected boolean horizontalTilesCollision(int i, Level level) {
        return horizontalCollision(i, level::isSolid);
    }

    protected boolean horizontalCollision(int i, BiIntPredicate condition) {
        for (int j = toTile(x()), jRight = toTile(x() + width - 1); j <= jRight; j++) {
            if (condition.test(i, j)) {
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
