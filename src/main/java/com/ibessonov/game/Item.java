package com.ibessonov.game;

import com.ibessonov.game.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.ibessonov.game.Constants.*;
import static com.ibessonov.game.resources.Resources.loadImage;

/**
 * @author ibessonov
 */
public class Item extends Rectangle implements Drawable, Disposable {

    public static final BufferedImage HEALTH_SPRITE = loadImage("health.png");
    private boolean disposed = false;

    public Item(int i, int j) {
        super(TILE, TILE);
        setPosition(j * TILE, i * TILE);
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        if (x - xOffset <= -width || x - xOffset >= SCREEN_WIDTH) {
            return;
        }
        if (y - yOffset <= -height || y - yOffset >= SCREEN_HEIGHT) {
            return;
        }
        new Sprite(0, 0, width, height, HEALTH_SPRITE)
                .draw(x - xOffset, y - yOffset, g);
    }

    public Player upgrade(Player player) {
        player.increaseLifeLevel(1);
        dispose();
        return player;
    }

    @Override
    public void dispose() {
        disposed = true;
    }

    @Override
    public boolean disposed() {
        return disposed;
    }
}
