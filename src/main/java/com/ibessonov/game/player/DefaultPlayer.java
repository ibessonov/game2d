package com.ibessonov.game.player;

import com.google.inject.Inject;
import com.ibessonov.game.*;

import java.awt.*;

import static com.ibessonov.game.Constants.TILE;

/**
 * @author ibessonov
 */
class DefaultPlayer extends Entity implements Player {

    @Inject
    private Keyboard keyboard;

    private int inJump;

    private boolean facingRight = true;
    private boolean facingDown = true;

    public DefaultPlayer() {
        super(TILE - 2, 2 * TILE - 4, 4, 1, 2);
    }

    @Override
    public int initialLifeLevel() {
        return 4;
    }

    public void handleJump(Level level) {
        if (inJump > 0 && !keyboard.isJumpPressed()) {
            interruptJumping();
        }
        if (keyboard.isJumpPressed()) {
            if (inJump > 0 && inJump <= 17) {
                speedY = level.gravity().directedSpeed(-jumpSpeed);
            }
        }
        if (super.handleJump(level, keyboard.isJumpTapped())) {
            inJump = 1;
//            level.gravity().flip(); inJump = 1000; // flips gravity on every jump
        }

        if (inJump > 0) {
            inJump++;
        }
        if (super.updateJump(level)) {
            inJump = (speedY == 0) ? 0 : 1000;
        }
    }

    @Override
    public void updateY(Level level) {
        if (level.gravity().isDown() != facingDown) {
            facingDown = level.gravity().isDown();
            inJump = 0;
        }
        this.handleJump(level);
    }

    @Override
    public void updateX(Level level) {
        super.updateRunSpeed(level, keyboard.isLeftPressed(), keyboard.isRightPressed());

        if (keyboard.isLeftPressed()) facingRight = false;
        if (keyboard.isRightPressed()) facingRight = true;
    }

    public SimpleBullet fireBullet() {
        SimpleBullet bullet = new SimpleBullet(0, 0, facingRight);

        int bulletX = x + (facingRight ? width + 9: -bullet.width() - 9);
        int bulletY = y + 12 + (facingDown ? 0 : 1);
        bullet.setPosition(bulletX, bulletY);

        return bullet;
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        Sprite s = new Sprite(2, 0, width, height, PlayerSprites.PLAYER_SPRITE);
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
