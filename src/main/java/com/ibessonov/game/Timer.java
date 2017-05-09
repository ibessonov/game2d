package com.ibessonov.game;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author ibessonov
 */
public class Timer {

    @SuppressWarnings("InfiniteLoopStatement")
    public static void run(Runnable callback, int fps) {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
        Thread writerThread = new Thread(() -> {
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
            } catch (InterruptedException e) {
                handleInterruptedException(e);
            }
        });

        Thread readerThread = new Thread(() -> {
            try {
                while (true) {
                    Runnable task = queue.take();
                    task.run();
                }
            } catch (InterruptedException e) {
                handleInterruptedException(e);
            }
        });

        readerThread.start();
        writerThread.start();
    }

    private static void handleInterruptedException(InterruptedException e) {
        e.printStackTrace();
        System.exit(1);
    }
}
