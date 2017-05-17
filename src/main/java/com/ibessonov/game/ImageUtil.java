package com.ibessonov.game;

import java.awt.image.BufferedImage;

/**
 * @author ibessonov
 */
public class ImageUtil {

    public static BufferedImage[] split(int w, int h, BufferedImage src) {
        int xs = src.getWidth() / w;
        int ys = src.getHeight() / h;
        BufferedImage[] result = new BufferedImage[xs * ys];

        int i = 0;
        for (int y = 0; y < ys; y++) {
            for (int x = 0; x < xs; x++) {
                result[i++] = src.getSubimage(x * w, y * h, w, h);
            }
        }
        return result;
    }
}
