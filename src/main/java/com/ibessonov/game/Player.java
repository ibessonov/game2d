package com.ibessonov.game;

import com.google.inject.Inject;

import static com.ibessonov.game.Constants.TILE;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author ibessonov
 */
public class Player {

    @Inject
    private Keyboard keyboard;

    public int x;
    public int y;

    public int width = TILE - 2;
    public int height = TILE - 2;

    public int speedX;
    public int speedY;
    public int inJump;

    public void updateRunSpeed(int[][] map) {
        if (speedX == 0) {
            if (keyboard.isRightPressed()) {
                speedX = 10;
            } else if (keyboard.isLeftPressed()) {
                speedX = -10;
            }
        } else if (speedX > 0) {
            if (keyboard.isRightPressed()) {
                speedX = min(20, speedX + 1);
            } else {
                speedX--;
                if (keyboard.isLeftPressed()) {
                    speedX--;
                }
            }
        } else {
            if (keyboard.isLeftPressed()) {
                speedX = max(-20, speedX - 1);
            } else {
                speedX++;
                if (keyboard.isRightPressed()) {
                    speedX++;
                }
            }
        }

        int xSpeedScaled = speedX / 10;
        x += xSpeedScaled;
        if (xSpeedScaled != 0) {
            if (leftCollision(map)) {
                int j = x / TILE;
                x = (j + 1) * TILE;
                speedX = 0;
            }
            if (rightCollision(map)) {
                int j = (x + width - 1) / TILE;
                x = j * TILE - width;
                speedX = 0;
            }
        }
    }

    public void handleJump(int[][] map) {
        if (inJump > 0 && !keyboard.isSpacePressed()) {
            inJump = 1000;
        }
        if (keyboard.isSpacePressed()) {
            if (inJump == 0 && isOnSurface(map)) {
                inJump = 1;
                speedY = -9;
            } else if (inJump > 0 && inJump <= 15) {
                speedY = -9;
            }
        }
    }

    public void updateJumpSpeed(int[][] map) {
        int ySpeedScaled = speedY / 3;
        y += ySpeedScaled;

        if (ySpeedScaled != 0) {
            if (ceilCollision(map)) {
                int i = y / TILE;
                y = (i + 1) * TILE;
                speedY = 0;
                inJump = 0;
            }
            if (floorCollision(map)) {
                int i = (y + height - 1) / TILE;
                y = i * TILE - height;
                speedY = 0;
                inJump = 0;
            }
        }
        speedY++;
        if (speedY > 45) {
            speedY = 45;
        }
        if (inJump > 0) {
            inJump++;
        }
    }

    private boolean ceilCollision(int[][] map) {
        int i = y / TILE;
        int jLeft = x / TILE;
        int jRight = (x + width - 1) / TILE;
        return map[i][jLeft] == 1 || map[i][jRight] == 1;
    }

    private boolean floorCollision(int[][] map) {
        int i = (y + height - 1) / TILE;
        int jLeft = x / TILE;
        int jRight = (x + width - 1) / TILE;
        return map[i][jLeft] == 1 || map[i][jRight] == 1;
    }

    private boolean leftCollision(int[][] map) {
        int j = x / TILE;
        int iCeil = y / TILE;
        int iFloor = (y + height - 1) / TILE;
        return map[iCeil][j] == 1 || map[iFloor][j] == 1;
    }

    private boolean rightCollision(int[][] map) {
        int j = (x + width - 1) / TILE;
        int iCeil = y / TILE;
        int iFloor = (y + height - 1) / TILE;
        return map[iCeil][j] == 1 || map[iFloor][j] == 1;
    }

    private boolean isOnSurface(int[][] map) {
        int i = (y + height) / TILE;
        int jLeft = x / TILE;
        int jRight = (x + width - 1) / TILE;
        return map[i][jLeft] == 1 || map[i][jRight] == 1;
    }
}
