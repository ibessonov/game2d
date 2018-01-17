package com.ibessonov.game;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ibessonov
 */
public class FrameHolder {

    private final AtomicInteger current = new AtomicInteger();

    public void tick() {
        current.getAndIncrement();
    }

    public int currentFrame() {
        return current.get();
    }
}
