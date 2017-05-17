package com.ibessonov.game;

import java.util.Comparator;

import static java.lang.Integer.compare;

public interface Centered {

    int centerX();
    int centerY();

    Comparator<Centered> COMPARATOR = (l, r) -> {
        int c = compare(l.centerX(), r.centerX());
        return c == 0 ? compare(l.centerY(), r.centerY()) : c;
    };
}
