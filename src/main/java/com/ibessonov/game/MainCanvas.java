package com.ibessonov.game;

import com.ibessonov.game.resources.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.function.Consumer;

import static com.ibessonov.game.Constants.SCREEN_HEIGHT;
import static com.ibessonov.game.Constants.SCREEN_WIDTH;
import static java.lang.Math.min;

class MainCanvas extends Canvas {

    private static final GraphicsDevice DEVICE = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getScreenDevices()[0];

    private JFrame jFrame = new JFrame();

    private final BufferedImage image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

    public MainCanvas() {
        jFrame.setIgnoreRepaint(true);
        setIgnoreRepaint(true);

        jFrame.setTitle("Gui");
        jFrame.setIconImage(Resources.loadImage("icon.png"));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
                }
            }
        });
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setPreferredSize(getMinimumSize());

        jFrame.setLayout(new BorderLayout());
        jFrame.add(this, BorderLayout.CENTER);

        jFrame.setExtendedState(jFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        jFrame.setUndecorated(true);
        DEVICE.setFullScreenWindow(jFrame);
        setPreferredSize(new Dimension(DEVICE.getDisplayMode().getWidth(), DEVICE.getDisplayMode().getHeight()));

        jFrame.pack();

        setFocusable(true);

        setBackground(Color.BLACK);
        createBufferStrategy(3);
        jFrame.setVisible(true);
    }

    public JFrame getJFrame() {
        return jFrame;
    }

    // these parameters suck
    public void render(Consumer<Graphics> callback, Consumer<int[]> postEffects) {
        {
            Graphics g = image.createGraphics();
            callback.accept(g);
            g.dispose();
        }

        int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        postEffects.accept(data);

        BufferStrategy bufferStrategy = getBufferStrategy();
        do {
            do {
                Graphics g = bufferStrategy.getDrawGraphics();
                g.drawImage(image, getXOffset(), getYOffset(),
                              SCREEN_WIDTH * getScale(), SCREEN_HEIGHT * getScale(), null);
                g.dispose();
            } while (bufferStrategy.contentsRestored());
            bufferStrategy.show();
        } while (bufferStrategy.contentsLost());
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
