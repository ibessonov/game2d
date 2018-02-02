package com.ibessonov.game;

import com.ibessonov.game.core.states.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import static com.ibessonov.game.Constants.*;
import static com.ibessonov.game.resources.Resources.loadImage;
import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.Math.min;
import static java.util.Arrays.asList;

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
        jFrame.setIconImages(asList(loadImage("icon.png"), loadImage("icon32.png"))); // yes, you can

        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jFrame.setLayout(new BorderLayout());

        canvas = new Canvas();
        canvas.setIgnoreRepaint(true);

        canvas.setMinimumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        canvas.setPreferredSize(canvas.getMinimumSize());

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

        Image cursorImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        jFrame.setCursor(getDefaultToolkit().createCustomCursor(cursorImage, new Point(), "Empty Mouse Cursor"));

        jFrame.add(canvas, BorderLayout.CENTER);
        jFrame.pack();

        canvas.setFocusable(true);
        canvas.requestFocus();
        canvas.createBufferStrategy(3);
        jFrame.setVisible(true);

        gameState = //new MenuGameState();
                    new GameStateImpl();
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
        Timer.run(this::tick, FPS);
    }

    public void stop() {
        Timer.stop();
    }
}
