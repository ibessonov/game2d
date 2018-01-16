package com.ibessonov.game.resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * @author ibessonov
 */
public class Resources {

    private static ClassLoader CL = Resources.class.getClassLoader();

    public static BufferedImage loadImage(String name) {
        try {
            URL resource = CL.getResource(name);
            if (resource == null) {
                throw new RuntimeException("Image file " + name + " not found");
            }
            return ImageIO.read(resource);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }
}
