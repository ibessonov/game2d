package com.ibessonov.game.player;

import com.google.inject.Inject;
import com.ibessonov.game.*;

import java.awt.*;

import static com.ibessonov.game.Constants.TILE;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author ibessonov
 */
class DefaultPlayer extends Entity implements Player {

    @Inject
    private Keyboard keyboard;

    private int inJump;

    private boolean facingRight = true;

    public DefaultPlayer() {
        super(TILE - 2, 2 * TILE - 4, 4, 3, 2);
        decreaseLifeLevel(3);
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
        if (handleJump(level, keyboard.isJumpTapped())) {
            inJump = 1;
//            level.gravity().flip(); inJump = 1000; // flips gravity on every jump
        }
    }

    public void updateJumpSpeed(Level level) {
        if (inJump > 0) {
            inJump++;
        }
        if (updateJump(level)) {
            inJump = (speedY == 0) ? 0 : 1000;
        }
    }

    public void update(Level levelMap) {
        this.handleJump(levelMap);
        this.updateJumpSpeed(levelMap);

        updateRunSpeed(levelMap, keyboard.isLeftPressed(), keyboard.isRightPressed());
        if (keyboard.isLeftPressed()) facingRight = false;
        if (keyboard.isRightPressed()) facingRight = true;
    }

    public Bullet fireBullet() {
        Bullet bullet = new Bullet(0, 0, facingRight);
        int bulletY = y + 8 + 4;
        if (facingRight) {
            bullet.setPosition(x + width - bullet.width(), bulletY);
        } else {
            bullet.setPosition(x, bulletY);
        }
        return bullet;
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        g.setColor(Color.RED);
        g.fillRect(x - xOffset, y - yOffset, width, height);

        g.setColor(new Color(128, 0, 0));
        g.drawRect(x - xOffset, y - yOffset, width - 1, height - 1);
    }

    public void interruptJumping() {
        inJump = 1000;
    }

    @Override
    public Player next() {
        return this;
    }
}
