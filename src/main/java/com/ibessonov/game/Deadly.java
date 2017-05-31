package com.ibessonov.game;

/**
 * @author ibessonov
 */
public interface Deadly extends Hazard {

    default int damage() {
        return Short.MAX_VALUE;
    }
}
