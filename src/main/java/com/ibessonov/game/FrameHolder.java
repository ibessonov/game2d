package com.ibessonov.game;

import javax.inject.Singleton;

/**
 * @author ibessonov
 */
@Singleton
public class FrameHolder {

    private volatile int current = 0;

    public void tick() {
        current++;
    }

    public int currentFrame() {
        return current;
    }
}
