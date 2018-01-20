package com.ibessonov.game;

/**
 * @author ibessonov
 */
public class FrameHolder {

    private int frame;

    public void tick() {
        frame++;
    }

    public int currentFrame() {
        return frame;
    }
}
