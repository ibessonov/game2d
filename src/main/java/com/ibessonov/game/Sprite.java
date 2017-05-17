package com.ibessonov.game;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    public void draw(int x, int y, boolean facingRight, boolean facingDown, Graphics g) {
        int iX = x;
        int iY = y;
        int iW = image.getWidth();
        int iH = image.getHeight();

        if (facingRight) {
            iX = iX - dx;
        } else {
            iX = iX + w + dx;
            iW = -iW;
        }

        if (facingDown) {
            iY = iY - dy;
        } else {
            iY = iY + h + dy;
            iH = -iH;
        }

        g.drawImage(image, iX, iY, iW, iH, null);
    }

    public void draw(int x, int y, Graphics g) {
        draw(x, y, true, true, g);
    }
}
