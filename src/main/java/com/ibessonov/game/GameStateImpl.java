package com.ibessonov.game;

import com.ibessonov.game.core.common.Disposable;
import com.ibessonov.game.core.common.util.BiIntPredicate;
import com.ibessonov.game.core.common.util.Container;
import com.ibessonov.game.core.states.GameState;
import com.ibessonov.game.enemies.BasicEnemy;
import com.ibessonov.game.player.DefaultPlayer;
import com.ibessonov.game.player.InvinciblePlayer;
import com.ibessonov.game.player.ProxyPlayer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.ibessonov.game.Constants.*;
import static com.ibessonov.game.Conversion.toScreen;
import static com.ibessonov.game.Conversion.toTile;
import static com.ibessonov.game.Effects.nightVision;
import static com.ibessonov.game.Effects.noise;
import static com.ibessonov.game.core.common.util.Container.*;
import static com.ibessonov.game.resources.Resources.loadImage;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author ibessonov
 */
public class GameStateImpl implements GameState {

    private final FrameHolder frame = new FrameHolder(); //TODO delete

    private Level level;

    private final ProxyPlayer player;

    private Collection<BasicEnemy> enemies = new ArrayList<>();
    private Collection<Item> items = new ArrayList<>();

    private List<Bullet> bullets = new ArrayList<>();

    private Container<Updatable> updatables;
    private Container<Disposable> disposables;
    private Container<Drawable> drawables;

    private int xOffset = 0;
    private int yOffset = 0;
    private boolean nightVision = false;

    private boolean pause = false;

    private final BufferedImage background = loadImage("back.png");

    public GameStateImpl() {
        player = new ProxyPlayer(new DefaultPlayer(frame));

        loadLevelData(new Level(1));
        player.setPosition(2 * TILE, (level.height() - 8) * TILE);
    }

    private void loadLevelData(Level level) {
        this.level = level;

        enemies.clear();
        items.clear();

//        ThreadLocalRandom rnd = ThreadLocalRandom.current();
//        for (int i = 0; i < 5; i++) {
//            BasicEnemy enemy = new BasicEnemy();
//            enemy.setPosition(toScreen(rnd.nextInt(level.width())),
//                    toScreen(rnd.nextInt(level.height())));
//            enemies.add(enemy);
//        }
//        for (int i = 0; i < 5; i++) {
//            items.add(new Item(rnd.nextInt(level.height()), rnd.nextInt(level.width())));
//        }

        updatables = join(list(level.platforms()), singleton(player), list(enemies), list(bullets));
        disposables = join(list(items), list(enemies), list(bullets));
        drawables = join(singleton(tiles(level, level::backTile)),
                list(level.platforms()),
                list(items), singleton(tiles(level, level::frontTile)), list(enemies), singleton(player), list(bullets)
        );
    }

    @Override
    public void update(Keyboard keyboard) {
        frame.tick();

        player.next();

        if (keyboard.isStartTapped()) {
//            pause = true;
            level.gravity().flip();
        }
//        if (keyboard.isNightVisionKeyTapped()) {
//            nightVision ^= true;
//        }

        if (keyboard.isFireTapped()/* || keyboard.isFirePressed()*/) {
            Bullet bullet = player.fireBullet(keyboard);
            if (bullet != null) {

                bullets.add(bullet);
            }
        }

        updatables.forEach(u -> u.updateY(level, keyboard));
        updatables.forEach(u -> u.updateX(level, keyboard));

        for (Item item : items) {
            if (item.intersects(player)) {
                player.transform(item::upgrade);
            }
        }

        for (BasicEnemy enemy : enemies) {
            for (Bullet bullet : bullets) {
                if (!bullet.disposed() && bullet.intersects(enemy)) {
                    enemy.decreaseLifeLevel(bullet.damage());
                    bullet.dispose();
                }
            }
            if (!enemy.disposed() && player.intersects(enemy)) {
                player.decreaseLifeLevel(enemy.damage());
                player.transform(InvinciblePlayer::transform);
            }
        }
        disposables.removeIf(Disposable::disposed);

        int playerLocalX = player.x() - xOffset;
        int playerLocalY = player.y() - yOffset;
        for (Trigger trigger : level.triggers()) {
            if (trigger.intersects(player)) {
                loadLevelData(new Level(trigger.levelIndex()));
                int playerX = player.x() - trigger.x() + trigger.toX();
                int playerY = player.y() - trigger.y() + trigger.toY();
                switch (trigger.type()) {
                    case HORIZONTAL:
                        if (playerLocalX > SCREEN_WIDTH / 2) {
                            playerLocalX -= SCREEN_WIDTH;
                        } else {
                            playerLocalX += SCREEN_WIDTH;
                        }
                        break;
                    case VERTICAL:
                        if (playerLocalY > SCREEN_HEIGHT / 2) {
                            playerLocalY -= SCREEN_HEIGHT;
                        } else {
                            playerLocalY += SCREEN_HEIGHT;
                        }
                        break;
                }
                xOffset = playerX - playerLocalX;
                xOffset = max(0, xOffset);
                xOffset = min(level.width() * TILE - SCREEN_WIDTH, xOffset);

                yOffset = playerY - playerLocalY;
                yOffset = max(0, yOffset);
                yOffset = min(level.height() * TILE - SCREEN_HEIGHT, yOffset);

                player.setPosition(playerX, playerY);
                return;
            }
        }

        int leftBorder = 9 * TILE; // ?
        int rightBorder = SCREEN_WIDTH - 9 * TILE - player.width();
        int dxOffset = 0;
        if (playerLocalX < leftBorder) {
            dxOffset = playerLocalX - leftBorder;
        }
        if (playerLocalX > rightBorder) {
            dxOffset = playerLocalX - rightBorder;
        }

        xOffset += dxOffset;
        xOffset = max(0, xOffset);
        xOffset = min(level.width() * TILE - SCREEN_WIDTH, xOffset);

        int upperBorder = 2 * TILE;
        int lowerBorder = SCREEN_HEIGHT - 3 * TILE - player.height();
        int dyOffset = 0;
        if (playerLocalY < upperBorder) {
            dyOffset = playerLocalY - upperBorder;
        }
        if (playerLocalY > lowerBorder) {
            dyOffset = playerLocalY - lowerBorder;
        }

//        if (Math.abs(dyOffset) > 4) { //TODO make it smooth?
//            dyOffset = Integer.signum(dyOffset) * 4;
//        }
        yOffset += dyOffset;
        yOffset = max(0, yOffset);
        yOffset = min(level.height() * TILE - SCREEN_HEIGHT, yOffset);
    }

    @Override
    public void render(BufferedImage image) {
        Graphics g = image.createGraphics();

        int dx = toScreen(level.width()) - SCREEN_WIDTH;
        int backX = dx == 0 ? 0 : xOffset * (background.getWidth() - SCREEN_WIDTH) / dx;
        int dy = toScreen(level.height()) - SCREEN_HEIGHT;
        int backY = dy == 0 ? 0 : yOffset * (background.getHeight() - SCREEN_HEIGHT) / dy;
        g.drawImage(background, -backX, -backY, null);

        drawables.forEach(d -> d.draw(g, xOffset, yOffset));

        for (int i = 0; i < player.initialLifeLevel(); i++) {
            g.setColor(i < player.currentLifeLevel() ? Color.WHITE : Color.DARK_GRAY);
            g.fillRect(6 + i * 5, 6, 4, 10);

            g.setColor(Color.RED);
            g.drawRect(6 + i * 5, 6, 3, 9);
        }
        g.dispose();

        postEffects(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
    }


    private void postEffects(int[] data) {
        if (nightVision) {
            nightVision(data);
            noise(data, 16);
        }
    }

    @Override
    public GameState next() {
        if (!pause) return this;

        pause = false;
        return new PauseGameState(this);
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
}
