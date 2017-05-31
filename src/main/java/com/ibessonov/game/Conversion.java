package com.ibessonov.game;

import static com.ibessonov.game.Constants.TILE;

/**
 * @author ibessonov
 */
public interface Conversion {

    static int toTile(int z) {
        if (z >= 0 || z % TILE == 0) return z / TILE;
        return (z / TILE) - 1;
    }

    static int toScreen(int k) {
        return k * TILE;
    }
}
