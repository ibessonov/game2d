package com.ibessonov.game;

import java.awt.*;

import static com.ibessonov.game.Constants.TILE;

/**
 * @author ibessonov
 */
public class Platform extends Rectangle implements InGameObject, Disposable, Drawable {

    public Platform(int w) {
        super(w * TILE, TILE);
    }

    private int counter = 0;
    private int radius = 50;
    private int max = 192;


    @Override
    public void updateY(Level level) {
        y = TILE * level.height() / 2 + (int) (radius * Math.sin(Math.PI * 2 * counter / max));
    }

    @Override
    public void updateX(Level level) {
        x = TILE * level.width() / 2 + (int) (radius * Math.cos(Math.PI * 2 * counter / max));
        counter++;
        if (counter == max) counter = 0;
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
