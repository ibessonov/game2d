package com.ibessonov.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.concurrent.ThreadLocalRandom;

import static com.ibessonov.game.Constants.TILE;
import static java.lang.Math.max;
import static java.lang.Math.min;

@Singleton
class MainCanvas extends Canvas {

    static GraphicsDevice device = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getScreenDevices()[0];

    @Inject
    private Keyboard keyboard;

    @Inject
    private Player player;

    private int scale;

    @Inject
    void initComponents() {
        JFrame jFrame = new JFrame("Gui");

        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        setPreferredSize(new Dimension(1280, 720));
//        setMaximumSize(new Dimension(640, 480));

        jFrame.setLayout(new BorderLayout());
        jFrame.add(this, BorderLayout.CENTER);

//        jFrame.setExtendedState(jFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
//        jFrame.setUndecorated(true);
//        device.setFullScreenWindow(jFrame);

        jFrame.pack();

        for (int i = 0; i < height; i++) {
            map[i][0] = 1;
            map[i][width - 1] = 1;
        }
        for (int j = 0; j < width; j++) {
            map[0][j] = 1;
            map[height - 1][j] = 1;
        }
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (ThreadLocalRandom.current().nextInt(4) == 0) {
                    map[i][j] = 1;
                }
            }
        }
        player.x = TILE;
        player.y = (height - 2) * TILE;

        addKeyListener(keyboard);
//        jFrame.setFocusTraversalKeysEnabled(true);
        setFocusable(true);

        scale = min(getWidth() / Constants.SCREEN_WIDTH, getHeight() / Constants.SCREEN_HEIGHT);

        createBufferStrategy(3);
        jFrame.setVisible(true);
        Timer.run(this::frame, 60);
    }

    private void update() {
        player.handleJump(map);
        player.updateJumpSpeed(map);

        player.updateRunSpeed(map);

        int playerLocalX = player.x - xOffset;
        int leftBorder = Constants.SCREEN_WIDTH / 2 - 2 * TILE;
        int rightBorder = Constants.SCREEN_WIDTH / 2 + TILE;
        int offset = 0;
        if (playerLocalX < leftBorder) {
            offset = playerLocalX - leftBorder;
        }
        if (playerLocalX > rightBorder) {
            offset = playerLocalX - rightBorder;
        }

        xOffset += offset;
        xOffset = max(0, xOffset);
        xOffset = min(width * TILE - Constants.SCREEN_WIDTH, xOffset);

        int playerLocalY = player.y - yOffset;
        int upperBorder = Constants.SCREEN_HEIGHT / 2 - 2 * TILE;
        int lowerBorder = Constants.SCREEN_HEIGHT / 2 + 1 * TILE;
        offset = 0;
        if (playerLocalY < upperBorder) {
            offset = playerLocalY - upperBorder;
        }
        if (playerLocalY > lowerBorder) {
            offset = playerLocalY - lowerBorder;
        }

        yOffset += offset;
        yOffset = max(0, yOffset);
        yOffset = min(height * TILE - Constants.SCREEN_HEIGHT, yOffset);
    }

    private final int height = 30;
    private final int width = 80;
    private final int[][] map = new int[height][width];

//    public static final int SCALE = 32;
    private int xOffset = 0;
    private int yOffset = 0;

    private final Color[] tilesPalette = { Color.CYAN, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.LIGHT_GRAY };
    private static int frame = 0;

    private void render() {
        BufferedImage image = new BufferedImage(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        for (int j = xOffset / TILE, x = j * TILE - xOffset; x < Constants.SCREEN_WIDTH; j++, x += TILE) {
            for (int i = yOffset / TILE, y = i * TILE - yOffset; y < Constants.SCREEN_HEIGHT; i++, y += TILE) {
                drawTile(map[i][j], g, x, y);
            }
        }
        drawPlayer(g);

        g.dispose();

        int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        drawEffects(data);

        BufferStrategy bs = getBufferStrategy();
        Graphics bsg = bs.getDrawGraphics();
        bsg.drawImage(image, (getWidth() - Constants.SCREEN_WIDTH * scale) / 2, (getHeight() - Constants.SCREEN_HEIGHT * scale) / 2,
                Constants.SCREEN_WIDTH * scale, Constants.SCREEN_HEIGHT * scale, null);
        bsg.dispose();
        bs.show();
    }

    private void drawEffects(int[] data) {
//        for (int i = 0; i < data.length; i++) {
//            int c = data[i];
//            int r = (c & 0xFF0000) >> 16;
//            int g = (c & 0x00FF00) >> 8;
//            int b = (c & 0x0000FF);
//            int grey = (int) (.2126 * r + .7152 * g + .0722 * b);
//            data[i] = grey | (grey << 8) | (grey << 16);
//        }
    }

    private void drawPlayer(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(player.x - xOffset, player.y - yOffset, player.width, player.height);
    }

    private void drawTile(int i, Graphics g, int x, int y) {
        g.setColor(tilesPalette[i]);
        g.fillRect(x, y, TILE, TILE);
    }


    public void frame() {
        this.render();
        this.update();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}
