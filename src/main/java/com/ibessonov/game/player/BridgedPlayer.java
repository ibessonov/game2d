package com.ibessonov.game.player;

import com.ibessonov.game.Level;
import com.ibessonov.game.Rectangular;
import com.ibessonov.game.SimpleBullet;

import java.awt.*;

/**
 * @author ibessonov
 */
public abstract class BridgedPlayer implements Player {

    protected Player delegate;

    protected BridgedPlayer(Player delegate) {
        this.delegate = delegate;
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
    public int centerX() {
        return delegate.centerX();
    }

    @Override
    public int centerY() {
        return delegate.centerY();
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
    public void decreaseLifeLevel(int damage) {
        delegate.decreaseLifeLevel(damage);
    }

    @Override
    public void increaseLifeLevel(int diff) {
        delegate.increaseLifeLevel(diff);
    }

    @Override
    public void updateY(Level level) {
        delegate.updateY(level);
    }

    @Override
    public void updateX(Level level) {
        delegate.updateX(level);
    }

    @Override
    public void draw(Graphics g, int xOffset, int yOffset) {
        delegate.draw(g, xOffset, yOffset);
    }

    @Override
    public SimpleBullet fireBullet() {
        return delegate.fireBullet();
    }

    @Override
    public boolean intersects(Rectangular other) {
        return delegate.intersects(other);
    }
}
