package com.ibessonov.game.player;


import java.awt.*;

public class InvinciblePlayer extends BridgedPlayer {

    private int tick = 0;
    private static final int DURATION = 120;
    private static final int FRAME = 60 / 6;

    private InvinciblePlayer(Player player) {
        super(player);
    }

    public static Player transform(Player player) { // how to be sure that player won't become invincible twice?
        return (player instanceof InvinciblePlayer) ? player : new InvinciblePlayer(player);
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        if ((tick / FRAME) % 2 == 0) super.draw(g, xOffset, yOffset);
    }

    @Override
    public void decreaseLifeLevel(int damage) {
    }

    @Override
    public Player next() {
        delegate = delegate.next();
        return ++tick < DURATION ? this : delegate;
    }
}
