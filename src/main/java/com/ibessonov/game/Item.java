package com.ibessonov.game;

import java.awt.*;

import static com.ibessonov.game.Constants.TILE;

/**
 * @author ibessonov
 */
public class Item extends Rectangle implements Drawable {

    public Item(int i, int j) {
        super(TILE, TILE);
        setPosition(j * TILE, i * TILE);
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        g.setColor(Color.WHITE);
        g.fillRect(x - xOffset, y - yOffset, width, height);

        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(x - xOffset, y - yOffset, width - 1, height - 1);
    }
}
