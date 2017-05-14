package com.ibessonov.game;

/**
 * @author ibessonov
 */
public class Rectangle implements Rectangular, Positionable {

    protected int x;
    protected int y;

    protected final int width;
    protected final int height;

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public final int x() {
        return x;
    }

    @Override
    public final int y() {
        return y;
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public final int width() {
        return width;
    }

    @Override
    public final int height() {
        return height;
    }
}
