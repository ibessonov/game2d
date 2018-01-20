package com.ibessonov.game;

import com.ibessonov.game.core.states.GameState;
import com.ibessonov.game.resources.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import static com.ibessonov.game.Constants.SCREEN_HEIGHT;
import static com.ibessonov.game.Constants.SCREEN_WIDTH;
import static java.lang.Math.min;

/**
 * @author ibessonov
 */
public class Game {

    private static final GraphicsDevice DEVICE = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getScreenDevices()[0];

    private final BufferedImage image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
    private final Canvas canvas;

    private final KeyboardImpl keyboard;

    private GameState gameState;

    public Game() {
        JFrame jFrame = new JFrame();
        jFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });

        jFrame.setIgnoreRepaint(true);

        jFrame.setTitle("Gui");
        jFrame.setIconImage(Resources.loadImage("icon.png"));

        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jFrame.setLayout(new BorderLayout());

        canvas = new Canvas();
        canvas.setIgnoreRepaint(true);

        canvas.setMinimumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        canvas.setPreferredSize(canvas.getMinimumSize());

        canvas.setFocusable(true);

        canvas.setBackground(Color.BLACK);

        //TODO not conventional, there's another keyboard listener for every purpose
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
                }
            }
        });
        keyboard = new KeyboardImpl(canvas);

        jFrame.setExtendedState(jFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        jFrame.setUndecorated(true);
        DEVICE.setFullScreenWindow(jFrame);
        canvas.setPreferredSize(new Dimension(DEVICE.getDisplayMode().getWidth(), DEVICE.getDisplayMode().getHeight()));

        jFrame.add(canvas, BorderLayout.CENTER);
        jFrame.pack();

        canvas.createBufferStrategy(3);
        jFrame.setVisible(true);

        gameState = new GameStateImpl();
    }

    public void tick() {
        keyboard.poll();

        gameState.update(keyboard);
        gameState.render(image);
        gameState = gameState.next();

        BufferStrategy bs = canvas.getBufferStrategy();
        do {
            do {
                Graphics g = bs.getDrawGraphics();

                int scale = min(canvas.getWidth() / SCREEN_WIDTH, canvas.getHeight() / SCREEN_HEIGHT);
                int xOffset = (canvas.getWidth() - SCREEN_WIDTH * scale) / 2;
                int yOffset = (canvas.getHeight() - SCREEN_HEIGHT * scale) / 2;
                g.drawImage(image, xOffset, yOffset, SCREEN_WIDTH * scale, SCREEN_HEIGHT * scale, null);

                g.dispose();
            } while (bs.contentsRestored());
            bs.show();
        } while (bs.contentsLost());
    }

    public void start() {
        Timer.run(this::tick, 60);
    }

    public void stop() {
        Timer.stop();
    }
}
