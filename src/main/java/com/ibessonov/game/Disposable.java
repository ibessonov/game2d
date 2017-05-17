package com.ibessonov.game;

/**
 * @author ibessonov
 */
public interface Disposable {

    boolean disposed();

    default void dispose() {}
}
