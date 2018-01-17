package com.ibessonov.game;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author ibessonov
 */
public final class Timer {

    private static final BlockingQueue<Runnable> QUEUE = new ArrayBlockingQueue<>(1);

    @SuppressWarnings("InfiniteLoopStatement")
    private static final Thread READER = new Thread(() -> {
        try {
            while (true) {
                QUEUE.take().run();
            }
        } catch (InterruptedException ignored) {
        }
    });

    static {
        READER.start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void run(Runnable callback, int fps) {
        new Thread(() -> {
            int currentFrame = 0;
            try {
                while (true) {
                    int nextFrame = currentFrame + 1;
                    int timeout = (nextFrame * 1000 / fps) - (currentFrame * 1000 / fps);
                    Thread.sleep(timeout);
                    QUEUE.offer(callback);

                    if (nextFrame == fps) nextFrame = 0;
                    currentFrame = nextFrame;
                }
            } catch (InterruptedException ignored) {
            }
        }).start();
    }

    public static void stop() {
        READER.interrupt();
        try {
            READER.join();
        } catch (InterruptedException ignored) {
        }
    }
}
