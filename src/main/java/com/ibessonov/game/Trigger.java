package com.ibessonov.game;

/**
 * @author ibessonov
 */
public class Trigger extends Rectangle implements HasRectangularHitBox {

    private int levelIndex;
    private int toX;
    private int toY;
    private final TriggerType type;

    public Trigger(int x, int y, int width, int height, int levelIndex, int toX, int toY, TriggerType type) {
        super(width, height);
        this.type = type;
        setPosition(x, y);
        this.levelIndex = levelIndex;
        this.toX = toX;
        this.toY = toY;
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

    public TriggerType type() {
        return type;
    }

    public enum TriggerType {
        HORIZONTAL, VERTICAL, SEAMLESS
    }
}
