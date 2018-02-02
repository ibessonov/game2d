package com.ibessonov.game;

import com.ibessonov.game.core.physics.XDirection;

import java.awt.*;

import static com.ibessonov.game.core.physics.XDirection.LEFT;
import static com.ibessonov.game.core.physics.XDirection.RIGHT;

class HorizontalPlatform extends Rectangle implements Platform {

    private int start;
    private int speed;
    private int total;
    private XDirection xDirection;

    public HorizontalPlatform(int x, int y, int w, int h, int start, int speed, int total, boolean right) {
        super(w, h);
        setPosition(x, y);
        this.start = x + start;
        this.speed = speed;
        this.total = total;
        this.xDirection = right ? RIGHT : LEFT;
    }

    @Override
    public void updateX(Level level, Keyboard keyboard) {
        if (xDirection == RIGHT) {
            x += speed;
            if (x >= start + total - width) {
                x = start + total - width;
                xDirection = LEFT;
            }
        } else {
            x -= speed;
            if (x <= start) {
                x = start;
                xDirection = RIGHT;
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
