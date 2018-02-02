package com.ibessonov.game;

import com.ibessonov.game.core.physics.Gravity;
import com.ibessonov.game.core.physics.YDirection;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.ibessonov.game.Constants.TILE;
import static com.ibessonov.game.Trigger.TriggerType.*;
import static com.ibessonov.game.resources.Resources.loadImage;
import static java.util.Arrays.asList;
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
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };
    private final int[][] level2 = {
            {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
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
            {1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 3, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 3, 0, 1},
            {1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 1, 1},
            {1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 1, 0},
            {1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1}
    };
    private final int[][] level4 = {
            {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    private final int height;
    private final int width;
    private final int[][] map;
    private final int number;

    private List<Platform> platforms = new ArrayList<>();

    private final Gravity gravity = new Gravity(0.375f, 7f, YDirection.DOWN);

    public Level(int number) {
        this.number = number;
        map    = copy(number == 1 ? level1
                    : number == 2 ? level2
                    : number == 3 ? level3
                    :               level4);
        height = map.length;
        width  = map[0].length;

        if (number == 1) {
            platforms.add(new HorizontalPlatform(5 * TILE, 7 * TILE, 2 * TILE, TILE / 2,
                    0, 1, 11 * TILE, true));
        }
    }

    private static int[][] copy(int[][] array) {
        int[][] clone = new int[array.length][];
        for (int i = 0; i < array.length; i++) {
            clone[i] = array[i].clone();
        }
        return clone;
    }

    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    public List<Platform> platforms() {
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

    @Deprecated
    public boolean backTile(int i, int j) {
        return map[i][j] > 2;
    }

    @Deprecated
    public boolean frontTile(int i, int j) {
        return map[i][j] <= 2; // ? distinguish solid tile from back tile
    }

    public List<Trigger> triggers() {
        switch (number) {
            case 1:
                return asList(
                        new Trigger(20 *  TILE + 7, (11 - 5) * TILE, 1, 2 * TILE,
                            2, 7, (22 - 5) * TILE, HORIZONTAL),
                        new Trigger((20 - 3) * TILE, -14, TILE, 1,
                            3, (20 - 3) * TILE, 11 * TILE - 14, VERTICAL)
                );
            case 2:
                return asList(
                        new Trigger(-7 - 1, (22 - 5) * TILE, 1, 2 * TILE,
                            1, 20 * TILE - 7 - 1, (11 - 5) * TILE, HORIZONTAL),
                        new Trigger(11 * TILE, -3 * TILE, 1, 6 * TILE,
                            4, 11 * TILE, -3 * TILE, SEAMLESS)
                );
            case 3:
                return singletonList(new Trigger((20 - 3) * TILE, 11 * TILE + 14, TILE, 1,
                            1, (20 - 3) * TILE,  14, VERTICAL));
            case 4:
                return singletonList(new Trigger(8 * TILE, -3 * TILE, 1, 6 * TILE,
                        2, 8 * TILE, -3 * TILE, SEAMLESS));
            default:
                throw new UnsupportedOperationException();
        }
    }

    public void bulletHit(int i, int j, Bullet bullet) {
        if (!isOutOfBounds(i, j)) {
            map[i][j] = 0;
        }
    }

    private static class LevelData {

        private String id;

        private int width;
        private int height;

        private int globalX;
        private int globalY;

        private List<Trigger> triggers;
    }
}
