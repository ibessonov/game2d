package com.ibessonov.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ibessonov.game.enemies.BasicEnemy;
import com.ibessonov.game.player.InvinciblePlayer;
import com.ibessonov.game.player.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static com.ibessonov.game.Constants.*;
import static com.ibessonov.game.Conversion.toScreen;
import static com.ibessonov.game.Conversion.toTile;
import static com.ibessonov.game.player.Player.defaultPlayer;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author ibessonov
 */
@Singleton
public class Game {

    @Inject private MainCanvas canvas;
    @Inject private FrameHolder frame;

    @Inject private Keyboard keyboard;
    @Inject private Level level;
    private Player player = defaultPlayer();

    private Collection<BasicEnemy> enemies = new ArrayList<>();
    private Set<Item> items = new HashSet<>();

    @Inject private GoodList<Bullet> bullets;

    private int xOffset = 0;
    private int yOffset = 0;
    private boolean nightVision = false;

    @Inject private void init() {
        player.setPosition(TILE, (level.height() - 2) * TILE);

        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 0; i < 20; i++) {
            BasicEnemy enemy = new BasicEnemy();
            enemy.setPosition(toScreen(rnd.nextInt(level.width())),
                    toScreen(rnd.nextInt(level.height())));
            enemies.add(enemy);
        }
        for (int i = 0; i < 20; i++) {
            items.add(new Item(rnd.nextInt(level.height()), rnd.nextInt(level.width())));
        }
    }

    public void tick() {
        frame.tick();
        update();
        canvas.render(this::render, this::postEffects);
    }

    private void update() {
        player = player.next();

        if (keyboard.isKeyTapped(KeyEvent.VK_X)) {
            nightVision ^= true;
        }
        if (keyboard.isFlipGravityTapped()) {
            level.gravity().flip();
        }

        for (Platform platform : level.platforms().list()) {
            platform.updateY(level);
        }
        player.updateY(level);
        for (BasicEnemy enemy : enemies) {
            enemy.updateY(level);
        }
        for (Bullet bullet : bullets.list()) {
            bullet.updateY(level);
        }

        for (Platform platform : level.platforms().list()) {
            platform.updateX(level);
        }
        player.updateX(level);
        for (BasicEnemy enemy : enemies) {
            enemy.updateX(level);
        }
        for (Bullet bullet : bullets.list()) {
            bullet.updateX(level);
        }


        for (Item item : items) {
            if (item.intersects(player)) {
                player = item.upgrade(player);
            }
        }
        items.removeIf(Item::disposed);


        bullets.sort();
        for (BasicEnemy enemy : enemies) {
            for (Bullet bullet : bullets.findNearest(enemy.x - 5, enemy.x + enemy.width + 5)) {
                if (bullet.intersects(enemy)) {
                    enemy.decreaseLifeLevel(bullet.damage());
                    bullet.dispose();
                }
            }
            if (player.intersects(enemy)) {
                player.decreaseLifeLevel(enemy.damage());
                player = new InvinciblePlayer(player);
            }
        }
        enemies.removeIf(HasLifeLevel::isDead);
        bullets.disposeWaste();

        if (keyboard.isFireTapped() || keyboard.isKeyPressed(KeyEvent.VK_SHIFT)) {
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

    private void render(Graphics g) {
        for (int j = toTile(xOffset), x = j * TILE - xOffset; x < SCREEN_WIDTH; j++, x += TILE) {
            for (int i = toTile(yOffset), y = i * TILE - yOffset; y < SCREEN_HEIGHT; i++, y += TILE) {
                if (level.backTile(i, j)) level.drawTile(g, i, j, x, y);
            }
        }
        for (Platform platform : level.platforms().list()) {
            platform.draw(g, xOffset, yOffset);
        }
        for (Bullet bullet : bullets.list()) {
            bullet.draw(g, xOffset, yOffset);
        }
        player.draw(g, xOffset, yOffset);
        for (Item item : items) {
            item.draw(g, xOffset, yOffset);
        }
        for (BasicEnemy enemy : enemies) {
            enemy.draw(g, xOffset, yOffset);
        }
        for (int j = toTile(xOffset), x = j * TILE - xOffset; x < SCREEN_WIDTH; j++, x += TILE) {
            for (int i = toTile(yOffset), y = i * TILE - yOffset; y < SCREEN_HEIGHT; i++, y += TILE) {
                if (level.frontTile(i, j)) level.drawTile(g, i, j, x, y);
            }
        }
        for (int i = 0; i < player.initialLifeLevel(); i++) {
            g.setColor(i < player.currentLifeLevel() ? Color.WHITE : Color.DARK_GRAY);
            g.fillRect(6 + i * 5, 6, 4, 10);

            g.setColor(Color.RED);
            g.drawRect(6 + i * 5, 6, 3, 9);
        }
    }


    private void postEffects(int[] data) {
        int min = 255;
        int max = 0;
        if (nightVision) {
            int avg = 0;
            for (int c : data) {
                int r = r(c);
                int g = g(c);
                int b = b(c);
                int grey = (2126 * r + 7152 * g + 722 * b) / 10000;
                avg += grey;
                min = min(min, grey);
                max = max(max, grey);
            }
            avg /= data.length;
            for (int i = 0; i < data.length; i++) {
                int c = data[i];
                int r = r(c);
                int g = g(c);
                int b = (b(c));
                int grey = (2126 * r + 7152 * g + 722 * b) / 10000;
                if (grey <= avg) {
                    data[i] = ((grey - min) * 128 / (avg - min)) << 8;
                } else {
                    data[i] = ((grey - avg) * 128 / (max - avg) + 127) << 8;
                }
            }
            ThreadLocalRandom random = ThreadLocalRandom.current();
            for (int i = 0; i < data.length; i++) {
                int c = data[i];
                int o = -32;
                int r = r(c) + random.nextInt(o, 1 - o);
                int g = g(c) + random.nextInt(o, 1 - o);
                int b = (b(c)) + random.nextInt(o, 1 - o);
                data[i] = c(r, g, b);
            }
        }
    }

    private int r(int c) {
        return (c & 0xFF0000) >> 16;
    }

    private int g(int c) {
        return (c & 0x00FF00) >> 8;
    }

    private int b(int c) {
        return c & 0x0000FF;
    }

    private int c(int r, int g, int b) {
        r = max(0, min(255, r));
        g = max(0, min(255, g));
        b = max(0, min(255, b));
        return (r << 16) | (g << 8) | b;
    }
}
