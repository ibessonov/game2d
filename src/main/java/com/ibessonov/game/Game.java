package com.ibessonov.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ibessonov.game.enemies.BasicEnemy;
import com.ibessonov.game.player.InvinciblePlayer;
import com.ibessonov.game.player.ProxyPlayer;
import com.ibessonov.game.util.BiIntPredicate;
import com.ibessonov.game.util.Container;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import static com.ibessonov.game.Constants.*;
import static com.ibessonov.game.Conversion.toScreen;
import static com.ibessonov.game.Conversion.toTile;
import static com.ibessonov.game.Effects.*;
import static com.ibessonov.game.player.Player.defaultPlayer;
import static com.ibessonov.game.resources.Resources.loadImage;
import static com.ibessonov.game.util.Container.*;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author ibessonov
 */
@Singleton
public class Game {

    @Inject
    private MainCanvas canvas;
    @Inject
    private FrameHolder frame;

    @Inject
    private Keyboard keyboard;
    @Inject
    private Level level;

    private final ProxyPlayer player = new ProxyPlayer(defaultPlayer());

    private Collection<BasicEnemy> enemies = new ArrayList<>();
    private Collection<Item> items = new ArrayList<>();

    @Inject
    private GoodList<Bullet> bullets;

    private Container<Updatable> updatables;
    private Container<Disposable> disposables;
    private Container<Drawable> drawables;

    private int xOffset = 0;
    private int yOffset = 0;
    private boolean nightVision = false;

    private final BufferedImage background = loadImage("back.png");

    @Inject
    private void init() {
        canvas.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int i = toTile((yOffset + e.getY() - canvas.getYOffset()) / canvas.getScale());
                int j = toTile((xOffset + e.getX() - canvas.getXOffset()) / canvas.getScale());
                if (level.isOutOfBounds(i, j)) return;

                System.out.printf("Clicked on (%d, %d)\n", i, j);
            }
        });

        player.setPosition(2 * TILE, (level.height() - 2) * TILE);

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
        updatables = join(list(level.platforms().list()), singleton(player), list(enemies), list(bullets.list()));
        disposables = join(list(items), list(enemies), list(bullets.list()));
        drawables = join(singleton(tiles(level, level::backTile)),
                         list(level.platforms().list()), list(items), list(enemies),
                         list(bullets.list()), singleton(player),
                         singleton(tiles(level, level::frontTile))
        );
    }

    public void tick() {
        frame.tick();
        update();
        canvas.render(this::render, this::postEffects);
    }

    private void update() {
        player.next();

        if (keyboard.isKeyTapped(KeyEvent.VK_X)) {
            nightVision ^= true;
        }
        if (keyboard.isFlipGravityTapped()) {
            level.gravity().flip();
        }

        updatables.forEach(u -> u.updateY(level));
        updatables.forEach(u -> u.updateX(level));

        for (Item item : items) {
            if (item.intersects(player)) {
                player.transform(item::upgrade);
            }
        }

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
                player.transform(InvinciblePlayer::new);
            }
        }
        disposables.removeIf(Disposable::disposed);

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
        int lowerBorder = SCREEN_HEIGHT / 2 + 1 * TILE / 2;
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

    private static Drawable tiles(Level level, BiIntPredicate filter) {
        return (g, xOffset, yOffset) -> {
            for (int j = toTile(xOffset), x = j * TILE - xOffset; x < SCREEN_WIDTH; j++, x += TILE) {
                for (int i = toTile(yOffset), y = i * TILE - yOffset; y < SCREEN_HEIGHT; i++, y += TILE) {
                    if (filter.test(i, j)) level.drawTile(g, i, j, x, y);
                }
            }
        };
    }

    private void render(Graphics g) {
        int backX = xOffset * (background.getWidth() - SCREEN_WIDTH) / (toScreen(level.width()) - SCREEN_WIDTH);
        int backY = yOffset * (background.getHeight() - SCREEN_HEIGHT) / (toScreen(level.height()) - SCREEN_HEIGHT);
        g.drawImage(background, -backX, -backY, null);

        drawables.forEach(d -> d.draw(g, xOffset, yOffset));

        for (int i = 0; i < player.initialLifeLevel(); i++) {
            g.setColor(i < player.currentLifeLevel() ? Color.WHITE : Color.DARK_GRAY);
            g.fillRect(6 + i * 5, 6, 4, 10);

            g.setColor(Color.RED);
            g.drawRect(6 + i * 5, 6, 3, 9);
        }
    }


    private void postEffects(int[] data) {
        if (nightVision) {
            nightVision(data);
            noise(data, 16);
        }
        int[][] matrix = {
            {1, 2, 1},
            {2, 36, 2},
            {1, 2, 1}
        };
        blur(data, matrix);
//        blur(data, matrix);
    }

    public void start() {
        Timer.run(this::tick, 60);
    }

    public void stop() {
        Timer.stop();
    }
}
