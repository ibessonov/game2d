package com.ibessonov.game.player;

import com.ibessonov.game.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.ibessonov.game.Constants.TILE;
import static com.ibessonov.game.Conversion.toScreen;
import static com.ibessonov.game.Conversion.toTile;

/**
 * @author ibessonov
 */
public class DefaultPlayer extends Entity implements Player {

    private Keyboard keyboard;

    private FrameHolder frameHolder;

    private int inJump;

    protected boolean isOnLadder = false;

    public DefaultPlayer(Keyboard keyboard, FrameHolder frameHolder) {
        super(TILE - 2, 2 * TILE - 4, 3.625f, 1f, 1.875f, 0.25f);
        this.keyboard = keyboard;
        this.frameHolder = frameHolder;
    }

    private int animationSprite;
    private int lastAnimationFrame;

    @Override
    public int initialLifeLevel() {
        return 4;
    }

    @Override
    public void updateY(Level level) {
        if (isOnLadder) {
            super.updateY(level);
            if (keyboard.isUpPressed()) {
                y.add(-1);
            } else if (keyboard.isDownPressed()) {
                y.add(1);
            }
            speedY.set(0);
            super.updateYCollision(level);
            boolean jumpTapped = keyboard.isJumpTapped();
            if (jumpTapped || !isStillOnLadder(level)) {
                isOnLadder = false;
                handleJump(level, jumpTapped);
            }
        } else {
            if (level.gravity().isDown() != facingDown) {
                inJump = 0;
            }
            super.updateY(level);
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
            if (super.updateGravityAndCollisions(level)) {
                inJump = (speedY.floatValue() == 0f) ? 0 : 1000;
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

    private boolean isStillOnLadder(Level level) {
        return verticalCollision(toTile(x()), level::isLadder);
    }

    private boolean isOverLadder(Level level) {
        return verticalCollision(toTile(centerX() - 5), level::isLadder)
            && verticalCollision(toTile(centerX() + 4), level::isLadder);
    }

    @Override
    public void updateX(Level level) {
        updateRunSpeed(level, keyboard.isLeftPressed(), keyboard.isRightPressed());
    }

    @Override
    public void updateRunSpeed(Level level, boolean moveLeft, boolean moveRight) {
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
        super.updateRunSpeed(level, moveLeft, moveRight);
        if (isOverLadder(level) && (keyboard.isUpPressed() || keyboard.isDownPressed())) {
            isOnLadder = true;
            inJump = 0;
            speedX.set(0);
            x.set(toScreen(toTile(centerX())) + (TILE - width()) / 2);
            y.round();
        }
        if (speedX.floatValue() == 0f) {
            x.round();
        }
    }

    private int lastBulletFrame = 0;
    public SimpleBullet fireBullet() {
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
            if (facingRight) {
                speedX = 6;
            } else {
                speedX = -6;
            }
        }
        SimpleBullet bullet = new SimpleBullet(speedX, speedY);

        bullet.setOrientation(facingRight, facingDown); // graphics only
        if (speedY == 0) {
            int bulletX = x() + (facingRight ? width + 9 : -bullet.width() - 9);
            int bulletY = y() + 12 + (facingDown ? 0 : 1);
            bullet.setPosition(bulletX, bulletY);
        } else if (speedX == 0) {
            int bulletX = centerX() - bullet.width() / 2 + (facingRight ? 0 : -2);
            int bulletY = y() + (speedY > 0 ? height : -bullet.height());
            bullet.setPosition(bulletX, bulletY);
        } else {
            int bulletX1 = x() + (facingRight ? width + 9 : -bullet.width() - 9);
            int bulletY1 = y() + 12 + (facingDown ? 0 : 1);
            int bulletX2 = centerX() - bullet.width() / 2 + (facingRight ? 0 : -2);
            int bulletY2 = y() + (speedY > 0 ? height : -bullet.height());
            bullet.setPosition((bulletX1 + bulletX2) / 2, (bulletY1 + bulletY2) / 2);
        }

        return bullet;
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        BufferedImage playerSprite = PlayerSprites.PLAYER_SPRITE;
        if (animationSprite != -1) {
            playerSprite = PlayerSprites.PLAYER_RUN[animationSprite];
        }
        Sprite s = new Sprite(2, 0, width, height, playerSprite);
        s.draw(x() - xOffset, y() - yOffset, facingRight, facingDown, g);
    }

    public void interruptJumping() {
        inJump = 1000;
    }

    @Override
    public Player next() {
        return this;
    }
}
