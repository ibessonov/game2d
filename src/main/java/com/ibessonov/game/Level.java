package com.ibessonov.game;

import com.ibessonov.game.physics.Gravity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import static com.ibessonov.game.Constants.TILE;
import static com.ibessonov.game.resources.Resources.loadImage;

/**
 * @author ibessonov
 */
public class Level {

    private final int height = 50;
    private final int width = 80;
    private final int[][] map = new int[height][width];

    private GoodList<Platform> platforms = new GoodList<>();

    private final Gravity gravity = new Gravity(1, 8 * 3, 3, true);

    public Level() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (j * (j + 1 - width) == 0) {
                    map[i][j] = 2;
                }
                if (i * (i + 1 - height) == 0) {
                    map[i][j] = 1;
                }
                if (ThreadLocalRandom.current().nextInt(12) == 0) {
                    map[i][j] = 1;
                }
            }
        }
        map[height - 3][1] = 1;
        map[height - 3][3] =
        map[height - 4][3] =
        map[height - 5][3] =
        map[height - 6][3] = 3;
        map[height - 7][3] = 1;

        Platform p = new CircularPlatform(3 * TILE, TILE);
        platforms.add(p);
    }

    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    public GoodList<Platform> platforms() {
        return platforms;
    }

    public Gravity gravity() {
        return gravity;
    }

    public boolean isSolid(int i, int j) {
        return isOutOfBounds(i, j) || map[i][j] == 1;
    }

    public boolean isLadder(int i, int j) {
        return !isOutOfBounds(i, j) && map[i][j] == 3;
    }

    public boolean isOutOfBounds(int i, int j) {
        return i < 0 || i >= height
            || j < 0 || j >= width;
    }

    public static BufferedImage BLOCK_SPRITE = loadImage("block.png");
    public static BufferedImage LADDER_SPRITE = loadImage("ladder.png");

    public void drawTile(Graphics g, int i, int j, int x, int y) {
        if (map[i][j] == 3) {
            g.drawImage(LADDER_SPRITE, x, y, null);
        } else if (map[i][j] > 0) {
            g.drawImage(BLOCK_SPRITE, x, y, null);
        }
    }

    public boolean backTile(int i, int j) {
        return map[i][j] != 2;
    }

    public boolean frontTile(int i, int j) {
        return map[i][j] == 2;
    }
}
