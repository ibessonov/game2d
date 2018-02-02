package com.ibessonov.game.core.geometry;

import java.util.Comparator;

import static java.util.Comparator.comparingInt;

/**
 * @author ibessonov
 */
public interface Centered {

    int centerX();
    int centerY();

    Comparator<Centered> COMPARATOR = comparingInt(Centered::centerX).thenComparingInt(Centered::centerY);
}
