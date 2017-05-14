package com.ibessonov.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ibessonov.game.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.ibessonov.game.Constants.*;
import static com.ibessonov.game.Conversion.toScreen;
import static com.ibessonov.game.Conversion.toTile;
import static java.lang.Math.max;
import static java.lang.Math.min;

@Singleton
class MainCanvas extends Canvas {

    static GraphicsDevice device = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getScreenDevices()[0];

    @Inject private FrameHolder frame;
    @Inject private Keyboard keyboard;
    @Inject private Level level;
    @Inject private Player player;

    private Collection<BasicEnemy> enemies = new ArrayList<>();
    private Set<Item> items = new HashSet<>();
    private Set<Bullet> bullets = new HashSet<>();

    private int xOffset = 0;
    private int yOffset = 0;

    @Inject
    void initComponents() {
        JFrame jFrame = new JFrame("Gui");

        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        jFrame.setResizable(false);
        setMinimumSize(new Dimension(1920 * 2 / 3, 1080 * 2 / 3));
        setPreferredSize(getMinimumSize());

        jFrame.setLayout(new BorderLayout());
        jFrame.add(this, BorderLayout.CENTER);

//        jFrame.setExtendedState(jFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
//        jFrame.setUndecorated(true);
//        device.setFullScreenWindow(jFrame);

        jFrame.pack();


        player.setPosition(TILE, (level.height() - 2) * TILE);

        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 0; i < 10; i++) {
            BasicEnemy enemy = new BasicEnemy();
            enemy.setPosition(toScreen(rnd.nextInt(level.width())),
                              toScreen(rnd.nextInt(level.height())));
            enemies.add(enemy);
        }
        for (int i = 0; i < 50; i++) {
            items.add(new Item(rnd.nextInt(level.height()), rnd.nextInt(level.width())));
        }

        addKeyListener(keyboard);
//        jFrame.setFocusTraversalKeysEnabled(true);
        setFocusable(true);

        createBufferStrategy(3);
        jFrame.setVisible(true);
        Timer.run(this::tick, 60);
    }

    private void update() {
        if (keyboard.isFlipGravityTapped()) {
            level.gravity().flip();
        }
        player.update(level);
        for (BasicEnemy enemy : enemies) {
            enemy.update(level);
        }
        items.removeIf(item -> item.intersects(player));
        bullets.removeIf(bullet -> bullet.update(level));

        for (Iterator<Bullet> bulletIterator = bullets.iterator(); bulletIterator.hasNext(); ) {
            Bullet bullet = bulletIterator.next();
            for (BasicEnemy enemy : enemies) {
                if (bullet.intersects(enemy)) {
                    enemy.decreaseLifeLevel(bullet.damage());
                    bulletIterator.remove();
                    break;
                }
            }
        }
        enemies.removeIf(HasLifeLevel::isDead);

        if (keyboard.isFireTapped()) {
            bullets.add(player.fireBullet());
        }

        int playerLocalX = player.x() - xOffset;
        int leftBorder = SCREEN_WIDTH / 2 - 2 * TILE;
        int rightBorder = SCREEN_WIDTH / 2 + TILE;
        int offset = 0;
        if (playerLocalX < leftBorder) {
            offset = playerLocalX - leftBorder;
        }
        if (playerLocalX > rightBorder) {
            offset = playerLocalX - rightBorder;
        }

        xOffset += offset;
        xOffset = max(0, xOffset);
        xOffset = min(level.width() * TILE - SCREEN_WIDTH, xOffset);

        int playerLocalY = player.y() - yOffset;
        int upperBorder = SCREEN_HEIGHT / 2 - 7 * TILE / 2;
        int lowerBorder = SCREEN_HEIGHT / 2 + 4 * TILE / 2;
        offset = 0;
        if (playerLocalY < upperBorder) {
            offset = playerLocalY - upperBorder;
        }
        if (playerLocalY > lowerBorder) {
            offset = playerLocalY - lowerBorder;
        }

        yOffset += offset;
        yOffset = max(0, yOffset);
        yOffset = min(level.height() * TILE - SCREEN_HEIGHT, yOffset);
    }

    private void render() {
        BufferedImage image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        for (int j = toTile(xOffset), x = j * TILE - xOffset; x < SCREEN_WIDTH; j++, x += TILE) {
            for (int i = toTile(yOffset), y = i * TILE - yOffset; y < SCREEN_HEIGHT; i++, y += TILE) {
                level.drawTile(g, i, j, x, y);
            }
        }
        for (Bullet bullet : bullets) {
            bullet.draw(g, xOffset, yOffset);
        }
        player.draw(g, xOffset, yOffset);
        for (Item item : items) {
            item.draw(g, xOffset, yOffset);
        }
        for (BasicEnemy enemy : enemies) {
            enemy.draw(g, xOffset, yOffset);
        }
        for (int i = 0; i < player.initialLifeLevel(); i++) {
            g.setColor(i < player.currentLifeLevel() ? Color.WHITE : Color.DARK_GRAY);
            g.fillRect(6 + i * 5, 6, 4, 10);

            g.setColor(Color.RED);
            g.drawRect(6 + i * 5, 6, 3, 9);
        }

        g.dispose();

        int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        drawEffects(data);

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


    public void tick() {
        frame.tick();
        update();
        render();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}
