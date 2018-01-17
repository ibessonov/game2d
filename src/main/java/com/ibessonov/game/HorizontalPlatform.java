package com.ibessonov.game;

import java.awt.*;

class HorizontalPlatform extends Rectangle implements Platform {

    private int start;
    private int speed;
    private int total;
    private boolean right;

    public HorizontalPlatform(int x, int y, int w, int h, int start, int speed, int total, boolean right) {
        super(w, h);
        setPosition(x, y);
        this.start = x + start;
        this.speed = speed;
        this.total = total;
        this.right = right;
    }

    @Override
    public void updateX(Level level) {
        if (right) {
            x += speed;
            if (x >= start + total - width) {
                x = start + total - width;
                right = false;
            }
        } else {
            x -= speed;
            if (x <= start) {
                x = start;
                right = true;
            }
        }
    }

    @Override
    public boolean disposed() {
        return false;
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        g.setColor(Color.RED);
        g.drawRect(x - xOffset, y - yOffset, width - 1, height - 1);
    }
}
