package com.ibessonov.game.player;

import java.awt.image.BufferedImage;

import static com.ibessonov.game.resources.Resources.loadImage;

/**
 * @author ibessonov
 */
class PlayerSprites {

    public static BufferedImage PLAYER_SPRITE = loadImage("player.png");

    public static BufferedImage[] PLAYER_RUN = {
        loadImage("run/frame1.png"),
        loadImage("run/frame2.png"),
        loadImage("run/frame3.png"),
        loadImage("run/frame4.png")
    };
}
