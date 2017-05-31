package com.ibessonov.game;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author ibessonov
 */
public class Timer {

    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);

    @SuppressWarnings("InfiniteLoopStatement")
    private static final Thread readerThread = new Thread(() -> {
        try {
            while (true) {
                Runnable task = queue.take();
                task.run();
            }
        } catch (InterruptedException ignored) {
        }
    });

    static {
        readerThread.start();
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
                    queue.offer(callback);

                    if (nextFrame > fps) nextFrame = 1;
                    currentFrame = nextFrame;
                }
            } catch (InterruptedException ignored) {
            }
        }).start();
    }

    public static void stop() {
        readerThread.interrupt();
        try {
            readerThread.join();
        } catch (InterruptedException ignored) {
        }
    }
}
