package com.ibessonov.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ibessonov.game.resources.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    private JFrame jFrame;

    private final BufferedImage image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

    @Inject
    void initComponents(CloseHandler closeHandler) {
        jFrame.setTitle("Gui");
        jFrame.setIconImage(Resources.loadImage("icon.png"));

        jFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                closeHandler.handleClose();
            }
        });
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setPreferredSize(getMinimumSize());

        jFrame.setLayout(new BorderLayout());
        jFrame.add(this, BorderLayout.CENTER);

//        jFrame.setExtendedState(jFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
//        jFrame.setUndecorated(true);
//        device.setFullScreenWindow(jFrame);

        jFrame.pack();

        setFocusable(true);

        createBufferStrategy(3);
        jFrame.setVisible(true);
    }

    // these parameters suck
    public void render(Consumer<Graphics> callback, Consumer<int[]> postEffects) {
        Graphics g = image.getGraphics();

        callback.accept(g);
        g.dispose();

        int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        postEffects.accept(data);

        BufferStrategy bs = getBufferStrategy();
        Graphics bsg = bs.getDrawGraphics();
        bsg.setColor(Color.BLACK);
        bsg.fillRect(0, 0, getWidth(), getHeight());

        bsg.drawImage(image, getXOffset(), getYOffset(),
                SCREEN_WIDTH * getScale(), SCREEN_HEIGHT * getScale(), null);
        bsg.dispose();
        bs.show();
    }

    public int getXOffset() {
        return (getWidth() - SCREEN_WIDTH * getScale()) / 2;
    }

    public int getYOffset() {
        return (getHeight() - SCREEN_HEIGHT * getScale()) / 2;
    }

    public int getScale() {
        return min(getWidth() / SCREEN_WIDTH, getHeight() / SCREEN_HEIGHT);
    }
}
