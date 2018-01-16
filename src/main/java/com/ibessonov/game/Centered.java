package com.ibessonov.game;

import java.util.Comparator;

import static java.util.Comparator.comparingInt;

public interface Centered {

    int centerX();
    int centerY();

    Comparator<Centered> COMPARATOR = comparingInt(Centered::centerX).thenComparingInt(Centered::centerY);
}
