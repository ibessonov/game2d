package com.ibessonov.game;

import com.ibessonov.game.physics.Gravity;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.ibessonov.game.Constants.TILE;

/**
 * @author ibessonov
 */
public class Level {

    private final int height = 50;
    private final int width = 80;
    private final int[][] map = new int[height][width];

    private final Gravity gravity = new Gravity(1, 9 * 3, 3, true);

    private final Color[] tilesPalette = { Color.CYAN, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.LIGHT_GRAY };

    public Level() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (ThreadLocalRandom.current().nextInt(12) == 0) {
                    map[i][j] = 1;
                }
            }
        }
    }

    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    public Gravity gravity() {
        return gravity;
    }

    public boolean isSolid(int i, int j) {
        return isOutOfBounds(i, j) || map[i][j] == 1;
    }

    public boolean isOutOfBounds(int i, int j) {
        return i < 0 || i >= height
            || j < 0 || j >= width;
    }

    public void drawTile(Graphics g, int i, int j, int x, int y) {
        g.setColor(tilesPalette[map[i][j]]);
        g.fillRect(x, y, TILE, TILE);
    }
}
