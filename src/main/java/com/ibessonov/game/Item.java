package com.ibessonov.game;

import com.ibessonov.game.player.Player;

import java.awt.*;

import static com.ibessonov.game.Constants.TILE;
import static com.ibessonov.game.resources.Resources.loadImage;

/**
 * @author ibessonov
 */
public class Item extends Rectangle implements Drawable, Disposable {

    private boolean disposed = false;

    public Item(int i, int j) {
        super(TILE, TILE);
        setPosition(j * TILE, i * TILE);
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        new Sprite(0, 0, width, height, loadImage("health.png"))
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
