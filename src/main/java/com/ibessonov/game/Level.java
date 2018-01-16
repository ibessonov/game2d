package com.ibessonov.game;

import com.ibessonov.game.physics.Gravity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static com.ibessonov.game.Constants.TILE;
import static com.ibessonov.game.resources.Resources.loadImage;
import static java.util.Collections.singletonList;

/**
 * @author ibessonov
 */
public class Level {

    private final int[][] level1 = {
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };
    private final int[][] level2 = {
            {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };
    private final int[][] level3 = {
            {1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 1},
            {1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 1},
            {1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 1},
            {1, 0, 0, 0, 0, 3, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 3, 1, 1},
            {1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 1, 1},
            {1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 1, 0},
            {1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1}
    };

    private final int height;
    private final int width;
    private final int[][] map;
    private final int number;

    private GoodList<Platform> platforms = new GoodList<>();

    private final Gravity gravity = new Gravity(0.375f, 7f, true);

    public Level(int number) {
        this.number = number;
        map    = number == 1 ? level1
               : number == 2 ? level2
               :               level3;
        height = map.length;
        width  = map[0].length;
//        Platform p = new CircularPlatform(3 * TILE, TILE);
//        platforms.add(p);
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
        return !isOutOfBounds(i, j) && map[i][j] == 1;
    }

    public boolean isLadder(int i, int j) {
        return !isOutOfBounds(i, j) && map[i][j] == 3;
    }

    public void inc(int i, int j) {
        map[i][j] = (map[i][j] + 1) % 4;
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
        return map[i][j] > 2;
    }

    public boolean frontTile(int i, int j) {
        return map[i][j] <= 2; // ? distinguish solid tile from back tile
    }

    public java.util.List<Trigger> triggers() {
        switch (number) {
            case 1:
                return Arrays.asList(
                        new Trigger(20 *  TILE + 7, (11 - 5) * TILE, 1, 2 * TILE,
                            2, 7, (21 - 5) * TILE, true),
                        new Trigger((20 - 3) * TILE, -14, TILE, 1,
                            3, (20 - 3) * TILE, 11 * TILE - 14, false)
                );
            case 2:
                return singletonList(new Trigger(-7 - 1, (21 - 5) * TILE, 1, 2 * TILE,
                            1, 20 * TILE - 7 - 1, (11 - 5) * TILE, true));
            case 3:
                return singletonList(new Trigger((20 - 3) * TILE, 11 * TILE + 14, TILE, 1,
                            1, (20 - 3) * TILE,  14, false));
            default:
                throw new UnsupportedOperationException();
        }
    }
}
