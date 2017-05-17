package com.ibessonov.game.resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author ibessonov
 */
public class Resources {

    private static ClassLoader CL = Resources.class.getClassLoader();

    public static BufferedImage loadImage(String name) {
        try {
            return ImageIO.read(CL.getResource(name));
        } catch (IOException ignored) {
            return null;
        }
    }
}
