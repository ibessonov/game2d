package com.ibessonov.game.player;


import com.ibessonov.game.Hazard;
import com.ibessonov.game.Rectangular;

import java.awt.*;

public class InvinciblePlayer extends BridgedPlayer {

    private int tick = 0;
    private static final int DURATION = 120;
    private static final int FRAME = 60 / 6;

    public InvinciblePlayer(Player player) {
        super(player);
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        if ((tick / FRAME) % 2 == 0) super.draw(g, xOffset, yOffset);
    }

    @Override
    public boolean intersects(Rectangular other) {
        return !(other instanceof Hazard) && super.intersects(other);
    }

    @Override
    public Player next() {
        delegate = delegate.next();
        return ++tick < DURATION ? this : delegate;
    }
}
