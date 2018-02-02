package com.ibessonov.game.player;

import com.ibessonov.game.*;
import com.ibessonov.game.core.physics.SpeedInfo;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

import static com.ibessonov.game.Constants.TILE;
import static com.ibessonov.game.Conversion.toScreen;
import static com.ibessonov.game.Conversion.toTile;
import static com.ibessonov.game.core.physics.XDirection.RIGHT;
import static com.ibessonov.game.core.physics.YDirection.DOWN;
import static com.ibessonov.game.core.physics.YDirection.UP;

/**
 * @author ibessonov
 */
public class DefaultPlayer extends Entity implements Player {

    private FrameHolder frameHolder;

    private int inJump;

    protected boolean isOnLadder = false;
    private SpeedInfo speedInfo = new SpeedInfo(.25f, .25f, 1.875f);

    public DefaultPlayer(FrameHolder frameHolder) {
        super(TILE - 2, 2 * TILE - 4, 3.625f);
        this.frameHolder = frameHolder;
    }

    private int animationSprite;
    private int lastAnimationFrame;

    @Override
    public int initialLifeLevel() {
        return 4;
    }

    @Override
    public void updateY(Level level, Keyboard keyboard) {
        if (isOnLadder) {
            super.updateY(level, keyboard);
            if (keyboard.isUpPressed()) {
                speedY.set(-1);
            } else if (keyboard.isDownPressed()) {
                speedY.set(1);
            }
            y.add(speedY);
            super.updateYCollision(level);
            speedY.set(0);
            boolean jumpTapped = keyboard.isJumpTapped();
            if (jumpTapped || !isStillOnLadder(level)) {
                isOnLadder = false;
                handleJump(level, jumpTapped);
            }
        } else {
            if (level.gravity().yDirection() != yDirection) {
                inJump = 0;
            }
            super.updateY(level, keyboard);
            if (inJump > 0 && !keyboard.isJumpPressed()) {
                interruptJumping();
            }
            if (keyboard.isJumpPressed()) {
                if (inJump > 0 && inJump <= 12) {
                    speedY.set(level.gravity().directedSpeed(-jumpSpeed.floatValue()));
                }
            }
            handleJump(level, keyboard.isJumpTapped());

            if (inJump > 0) {
                inJump++;
            }
            updateGravity(level);
            if (updateYCollision(level)) {
                inJump = (speedY.floatValue() == 0f) ? 0 : 1000;
            } else {
                if (yDirection == DOWN && speedY.floatValue() > 0
                        && horizontalCollision(toTile(y() + height - 1), level::isLadder)
                        && !horizontalCollision(toTile(y() + height - 1 - speedY.intValue()), level::isLadder)) {
                    inJump = 0;
                    speedY.set(0);
                    y.set(toScreen(toTile(y() + height - 1)) - height);
                }
                if (yDirection == UP && speedY.floatValue() < 0
                        && horizontalCollision(toTile(y()), level::isLadder)
                        && !horizontalCollision(toTile(y() - speedY.intValue()), level::isLadder)) {
                    inJump = 0;
                    speedY.set(0);
                    y.set(toScreen(toTile(y()) + 1));
                }
            }
        }
    }

    @Override
    public boolean handleJump(Level level, boolean jump) {
        if (super.handleJump(level, jump)) {
            inJump = 1;
//            level.gravity().flip(); inJump = 1000; // flips gravity on every jump
            return true;
        }
        return false;
    }

    @Override
    protected boolean isOnSurface(Level level) {
        if (super.isOnSurface(level)) {
            return true;
        }
        // ability to jump from the top of the ladder
        if (yDirection == DOWN) {
            return horizontalCollision(toTile(y() + height), level::isLadder)
               && !horizontalCollision(toTile(y() + height - 1), level::isLadder);
        } else {
            return horizontalCollision(toTile(y() - 1), level::isLadder)
               && !horizontalCollision(toTile(y()), level::isLadder);
        }
    }

    private boolean isStillOnLadder(Level level) {
        return verticalCollision(toTile(x()), level::isLadder);
    }

    private boolean isOverLadder(Level level) {
        return verticalCollision(toTile(x()), level::isLadder)
            && verticalCollision(toTile(x() + width() - 1), level::isLadder);
    }

    @Override
    public void updateX(Level level, Keyboard keyboard) {
        updateRunSpeed(level, keyboard, keyboard.isLeftPressed(), keyboard.isRightPressed());
    }

    @Override
    public void updateRunSpeed(Level level, Keyboard keyboard, boolean moveLeft, boolean moveRight) {
        if (isOnLadder && !isOnSurface(level)) {
            updateHorizontalFacing(moveLeft, moveRight);
            // animation
            return;
        }
        if (moveLeft || moveRight) {
            if (animationSprite == -1) {
                lastAnimationFrame = frameHolder.currentFrame();
                animationSprite = 0;
            } else {
                if (frameHolder.currentFrame() - lastAnimationFrame > 9) {
                    lastAnimationFrame = frameHolder.currentFrame();
                    animationSprite = (animationSprite + 1) % PlayerSprites.PLAYER_RUN.length;
                }
            }
        } else {
            animationSprite = -1;
        }
        if (isOnLadder && (moveLeft || moveRight)) {
            isOnLadder = false;
        }
        super.updateRunSpeed(level, keyboard, moveLeft, moveRight);
        if ((keyboard.isUpPressed() || keyboard.isDownPressed()) && isOverLadder(level)) {
            climbLadder();
        }

        if (yDirection == DOWN && keyboard.isDownPressed() && horizontalCollision(toTile(y() + height), level::isLadder)) {
            y.add(1);
            if (isOverLadder(level)) {
                climbLadder();
            } else {
                y.add(-1);
            }
        }
        if (yDirection == UP && keyboard.isUpPressed() && horizontalCollision(toTile(y() - 1), level::isLadder)) {
            y.add(-1);
            if (isOverLadder(level)) {
                climbLadder();
            } else {
                y.add(1);
            }
        }

        if (speedX.floatValue() == 0f) {
            x.round();
        }
    }

    @Override
    protected void accelerate(Keyboard keyboard, boolean moveLeft, boolean moveRight) {
        speedInfo.update(speedX, moveLeft, moveRight);
    }

    private void climbLadder() {
        isOnLadder = true;
        inJump = 0;
        speedX.set(0);
        speedY.set(0);
        x.set(toScreen(toTile(centerX())) + (TILE - width()) / 2);
        y.round();
    }

    private int lastBulletFrame = 0;
    @Override
    public Bullet fireBullet(Keyboard keyboard) {
        if (frameHolder.currentFrame() - lastBulletFrame < 6) {
            return null;
        }
        lastBulletFrame = frameHolder.currentFrame();

        int speedX = 0;
        int speedY = 0;
        if (keyboard.isUpPressed()) {
            speedY = -6;
        } else if (keyboard.isDownPressed()) {
            speedY = 6;
        }
        if (speedY != 0) {
            speedY = speedY * 2 / 3;
            if (keyboard.isRightPressed()) {
                speedX = 4;
            } else if (keyboard.isLeftPressed()) {
                speedX = -4;
            }
        } else {
            if (xDirection == RIGHT) {
                speedX = 6;
            } else {
                speedX = -6;
            }
        }
        SimpleBullet bullet = new SimpleBullet(speedX, speedY);

        bullet.setOrientation(xDirection, yDirection); // graphics only
        if (speedY == 0) {
            int bulletX = x() + (xDirection == RIGHT ? width + 9 : -bullet.width() - 9);
            int bulletY = y() + 12 + (yDirection == DOWN ? 0 : 1);
            bullet.setPosition(bulletX - speedX, bulletY - speedY);
        } else if (speedX == 0) {
            int bulletX = centerX() - bullet.width() / 2 + (xDirection == RIGHT ? 0 : -2);
            int bulletY = y() + (speedY > 0 ? height : -bullet.height());
            bullet.setPosition(bulletX - speedX, bulletY - speedY);
        } else {
            int bulletX1 = x() + (xDirection == RIGHT ? width + 9 : -bullet.width() - 9);
            int bulletY1 = y() + 12 + (yDirection == DOWN ? 0 : 1);
            int bulletX2 = centerX() - bullet.width() / 2 + (xDirection == RIGHT ? 0 : -2);
            int bulletY2 = y() + (speedY > 0 ? height : -bullet.height());
            bullet.setPosition((bulletX1 + bulletX2) / 2 - speedX, (bulletY1 + bulletY2) / 2 - speedY);
        }

        return bullet;
    }

    private List<Bullet> fireBullet(int centerX, int centerY, int directionX, int directionY) {
        double angle = Math.atan2(directionY, directionX);
        return Collections.emptyList();
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        BufferedImage playerSprite = PlayerSprites.PLAYER_SPRITE;
        if (animationSprite != -1) {
            playerSprite = PlayerSprites.PLAYER_RUN[animationSprite];
        }
        Sprite s = new Sprite(2, 0, width, height, playerSprite);
        s.draw(x() - xOffset, y() - yOffset, xDirection, yDirection, g);
    }

    public void interruptJumping() {
        inJump = 1000;
    }

    @Override
    public Player next() {
        return this;
    }
}
