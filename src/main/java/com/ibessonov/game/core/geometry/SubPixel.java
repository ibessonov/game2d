package com.ibessonov.game.core.geometry;

/**
 * @author ibessonov
 */
public final class SubPixel {

    private static final int SCALE = 8;

    private int value;

    private SubPixel(int value) {
        this.value = value;
    }


    public static SubPixel subPixel(int value) {
        return new SubPixel(value * SCALE);
    }

    public static SubPixel subPixel(float value) {
        return new SubPixel(Math.round(value * SCALE));
    }


    public int intValue() {
        return Math.round(floatValue());
    }

    public float floatValue() {
        return 1f * this.value / SCALE;
    }


    public void round() {
        set(intValue());
    }


    public void set(int value) {
        this.value = value * SCALE;
    }

    public void set(float value) {
        this.value = Math.round(value * SCALE);
    }

    public void set(SubPixel subPixel) {
        this.value = subPixel.value;
    }


    public void add(int diff) {
        this.value += diff * SCALE;
    }

    public void add(float diff) {
        this.value += Math.round(diff * SCALE);
    }

    public void add(SubPixel diff) {
        this.value += diff.value;
    }
}
