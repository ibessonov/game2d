package com.ibessonov.game;

import java.util.concurrent.ThreadLocalRandom;

import static com.ibessonov.game.Constants.SCREEN_HEIGHT;
import static com.ibessonov.game.Constants.SCREEN_WIDTH;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.IntStream.range;

/**
 * @author ibessonov
 */
public class Effects {

    static int buffer[] = new int[SCREEN_WIDTH * SCREEN_HEIGHT];

    public static void nightVision(int[] data) {
        int min = 255;
        int max = 0;
        int avg = 0;
        for (int c : data) {
            int r = r(c);
            int g = g(c);
            int b = b(c);
            int grey = greyScale(r, g, b);
            avg += grey;
            min = min(min, grey);
            max = max(max, grey);
        }
        avg /= data.length;
        for (int i = 0; i < data.length; i++) {
            int c = data[i];
            int r = r(c);
            int g = g(c);
            int b = b(c);
            int grey = greyScale(r, g, b);
            int mid = 96;
            if (grey <= avg) {
                data[i] = ((grey - min) * mid / (avg - min)) << 8;
            } else {
                data[i] = ((grey - avg) * (255 - mid) / (max - avg) + mid) << 8;
            }
        }
    }

    static void noise(int[] data, int d) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < data.length; i++) {
            int c = data[i];
            int r = r(c) + random.nextInt(-d, d - 1);
            int g = g(c) + random.nextInt(-d, d - 1);
            int b = b(c) + random.nextInt(-d, d - 1);
            data[i] = c(r, g, b);
        }
    }

    public static void blur(int[] data, int[][] matrix) {
        int sum = 0;
        for (int[] ints : matrix) for (int i : ints) sum += i;
        int finalSum = sum;
        int h = matrix.length;
        int w = matrix[0].length;

        range(0, SCREEN_HEIGHT).parallel().forEach(i -> {
            for (int j = 0; j < SCREEN_WIDTH; j++) {
                int s = finalSum;
                int r = 0, g = 0, b = 0;
                for (int ii = i - h/2; ii < i - h/2 + h; ii++) {
                    for (int jj = j - w/2; jj < j - w/2 + w; jj++) {
                        int v = matrix[ii - i + h/2][jj - j + w/2];
                        if (ii < 0 || ii >= SCREEN_HEIGHT || jj < 0 || jj >= SCREEN_WIDTH) {
                            s -= v;
                            continue;
                        }
                        int c = data[ii * SCREEN_WIDTH + jj];
                        r += v * r(c);
                        g += v * g(c);
                        b += v * b(c);
                    }
                }

                buffer[i * SCREEN_WIDTH + j] = c(r / s, g / s, b / s);
            }
        });
        System.arraycopy(buffer, 0, data, 0, data.length);
    }

    public static int greyScale(int r, int g, int b) {
        return (2126 * r + 7152 * g + 722 * b) / 10000; // Standard sRGB weights
    }

    public static int r(int c) {
        return (c & 0xFF0000) >> 16;
    }

    public static int g(int c) {
        return (c & 0x00FF00) >> 8;
    }

    public static int b(int c) {
        return c & 0x0000FF;
    }

    public static int c(int r, int g, int b) {
        r = max(0, min(255, r));
        g = max(0, min(255, g));
        b = max(0, min(255, b));
        return (r << 16) | (g << 8) | b;
    }
}
