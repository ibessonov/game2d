package com.ibessonov.game;

/**
 * @author ibessonov
 */
public class Trigger extends Rectangle {

    private int levelIndex;
    private int toX;
    private int toY;
    private boolean horizontal;

    public Trigger(int x, int y, int width, int height, int levelIndex, int toX, int toY, boolean horizontal) {
        super(width, height);
        setPosition(x, y);
        this.levelIndex = levelIndex;
        this.toX = toX;
        this.toY = toY;
        this.horizontal = horizontal;
    }

    public int levelIndex() {
        return levelIndex;
    }

    public int toX() {
        return toX;
    }

    public int toY() {
        return toY;
    }

    public boolean horizontal() {
        return horizontal;
    }

    //TODO support triggers for seamless transitions
    public boolean vertical() {
        return !horizontal;
    }
}
