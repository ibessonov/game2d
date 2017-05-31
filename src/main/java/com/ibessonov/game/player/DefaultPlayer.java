package com.ibessonov.game.player;

import com.google.inject.Inject;
import com.ibessonov.game.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.ibessonov.game.Constants.TILE;
import static com.ibessonov.game.Conversion.toScreen;
import static com.ibessonov.game.Conversion.toTile;

/**
 * @author ibessonov
 */
class DefaultPlayer extends Entity implements Player {

    @Inject
    private Keyboard keyboard;

    @Inject
    private FrameHolder frameHolder;

    private int inJump;

    protected boolean isOnLadder = false;

    public DefaultPlayer() {
        super(TILE - 2, 2 * TILE - 4, 4, 1, 2);
    }

    private int animationSprite;
    private int lastAnimationFrame;

    @Override
    public int initialLifeLevel() {
        return 8;
    }

    @Override
    public void updateY(Level level) {
        if (isOnLadder) {
            super.updateY(level);
            if (keyboard.isUpPressed()) {
                y--;
            } else if (keyboard.isDownPressed()) {
                y++;
            }
            super.updateGravityAndCollisions(level);
            boolean jumpTapped = keyboard.isJumpTapped();
            if (jumpTapped || !isStillOnLadder(level)) {
                isOnLadder = false;
                handleJump(level, jumpTapped);
            }
            speedY = 0;
        } else {
            if (level.gravity().isDown() != facingDown) {
                inJump = 0;
            }
            super.updateY(level);
            if (inJump > 0 && !keyboard.isJumpPressed()) {
                interruptJumping();
            }
            if (keyboard.isJumpPressed()) {
                if (inJump > 0 && inJump <= 17) {
                    speedY = level.gravity().directedSpeed(-jumpSpeed);
                }
            }
            handleJump(level, keyboard.isJumpTapped());

            if (inJump > 0) {
                inJump++;
            }
            if (super.updateGravityAndCollisions(level)) {
                inJump = (speedY == 0) ? 0 : 1000;
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
        super.updateRunSpeed(level, moveLeft, moveRight);
        if (isOverLadder(level) && (keyboard.isUpPressed() || keyboard.isDownPressed())) {
            isOnLadder = true;
            speedX.stop();
            x = toScreen(toTile(centerX())) + (TILE - width()) / 2;
        }
    }

    public SimpleBullet fireBullet() {
        SimpleBullet bullet = new SimpleBullet(0, 0, facingRight);

        int bulletX = x + (facingRight ? width + 9: -bullet.width() - 9);
        int bulletY = y + 12 + (facingDown ? 0 : 1);
        bullet.setPosition(bulletX, bulletY);
        bullet.setOrientation(facingRight, facingDown);

        return bullet;
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        BufferedImage playerSprite = PlayerSprites.PLAYER_SPRITE;
        if (animationSprite != -1) {
            playerSprite = PlayerSprites.PLAYER_RUN[animationSprite];
        }
        Sprite s = new Sprite(2, 0, width, height, playerSprite);
        s.draw(x - xOffset, y - yOffset, facingRight, facingDown, g);
    }

    public void interruptJumping() {
        inJump = 1000;
    }

    @Override
    public Player next() {
        return this;
    }
}
