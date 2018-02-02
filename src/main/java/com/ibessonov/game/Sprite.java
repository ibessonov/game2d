package com.ibessonov.game;

import com.ibessonov.game.core.physics.XDirection;
import com.ibessonov.game.core.physics.YDirection;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.ibessonov.game.core.physics.XDirection.RIGHT;
import static com.ibessonov.game.core.physics.YDirection.DOWN;

/**
 * @author ibessonov
 */
public class Sprite {

    private final int dx;
    private final int dy;

    private final int w;
    private final int h;

    private final BufferedImage image;

    public Sprite(int dx, int dy, int w, int h, BufferedImage image) {
        this.dx = dx;
        this.dy = dy;
        this.w = w;
        this.h = h;
        this.image = image;
    }

    public void draw(int x, int y, XDirection xDirection, YDirection yDirection, Graphics g) {
        int iX = x;
        int iY = y;
        int iW = image.getWidth();
        int iH = image.getHeight();

        if (xDirection == RIGHT) {
            iX = iX - dx;
        } else {
            iX = iX + w + dx;
            iW = -iW;
        }

        if (yDirection == DOWN) {
            iY = iY - dy;
        } else {
            iY = iY + h + dy;
            iH = -iH;
        }

        g.drawImage(image, iX, iY, iW, iH, null);
    }

    public void draw(int x, int y, Graphics g) {
        draw(x, y, RIGHT, DOWN, g);
    }
}
