package com.ibessonov.game.player;

import com.google.inject.Inject;
import com.ibessonov.game.Bullet;
import com.ibessonov.game.Level;

import java.awt.*;

/**
 * @author ibessonov
 */
public class BridgedPlayer implements Player {

    private Player delegate;

    @Inject
    BridgedPlayer(DefaultPlayer defaultPlayer) {
        this.delegate = defaultPlayer;
    }

    @Override
    public int x() {
        return delegate.x();
    }

    @Override
    public int y() {
        return delegate.y();
    }

    @Override
    public void setPosition(int x, int y) {
        delegate.setPosition(x, y);
    }

    @Override
    public int width() {
        return delegate.width();
    }

    @Override
    public int height() {
        return delegate.height();
    }

    @Override
    public int initialLifeLevel() {
        return delegate.initialLifeLevel();
    }

    @Override
    public int currentLifeLevel() {
        return delegate.currentLifeLevel();
    }

    @Override
    public int decreaseLifeLevel(int damage) {
        return delegate.decreaseLifeLevel(damage);
    }

    @Override
    public void update(Level level) {
        delegate.update(level);
//        delegate = delegate.next();
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        delegate.draw(g, xOffset, yOffset);
    }

    @Override
    public Bullet fireBullet() {
        return delegate.fireBullet();
    }

    @Override
    public Player next() {
        return this;
    }
}
