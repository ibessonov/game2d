package com.ibessonov.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.function.Consumer;

import static com.ibessonov.game.Constants.SCREEN_HEIGHT;
import static com.ibessonov.game.Constants.SCREEN_WIDTH;
import static java.lang.Math.min;

@Singleton
class MainCanvas extends Canvas {

    static GraphicsDevice device = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getScreenDevices()[0];

    @Inject
    void initComponents() {
        JFrame jFrame = new JFrame("Gui");

        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        jFrame.setResizable(false);
        setMinimumSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        setPreferredSize(getMinimumSize());

        jFrame.setLayout(new BorderLayout());
        jFrame.add(this, BorderLayout.CENTER);

//        jFrame.setExtendedState(jFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
//        jFrame.setUndecorated(true);
//        device.setFullScreenWindow(jFrame);

        jFrame.pack();

//        jFrame.setFocusTraversalKeysEnabled(true);
        setFocusable(true);

        createBufferStrategy(3);
        jFrame.setVisible(true);
    }

    public void render(Consumer<Graphics> callback, Consumer<int[]> postEffects) {
        BufferedImage image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        callback.accept(g);
        g.dispose();

        int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        postEffects.accept(data);

        BufferStrategy bs = getBufferStrategy();
        Graphics bsg = bs.getDrawGraphics();
        bsg.setColor(Color.BLACK);
        bsg.fillRect(0, 0, getWidth(), getHeight());

        int scale = min(getWidth() / SCREEN_WIDTH, getHeight() / SCREEN_HEIGHT);
        bsg.drawImage(image, (getWidth() - SCREEN_WIDTH * scale) / 2, (getHeight() - SCREEN_HEIGHT * scale) / 2,
                SCREEN_WIDTH * scale, SCREEN_HEIGHT * scale, null);
        bsg.dispose();
        bs.show();
    }
}
