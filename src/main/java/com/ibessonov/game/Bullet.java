package com.ibessonov.game;

import java.awt.*;

/**
 * @author ibessonov
 */
public class Bullet extends Entity {

    public Bullet(int x, int y, boolean facingRight) {
        super(5, 3, 0, 4, 4);
        setPosition(x, y);
        speedX.update(!facingRight, facingRight);
    }

    public int damage() {
        return 5;
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        g.setColor(Color.WHITE);
        g.fillRect(x - xOffset, y - yOffset, width, height);

        g.setColor(Color.RED);
        g.drawRect(x - xOffset, y - yOffset, width - 1, height - 1);
    }

    public boolean update(Level level) {
        updateRunSpeed(level, speedX.value() < 0, speedX.value() > 0);
        return speedX.value() == 0;
    }
}
