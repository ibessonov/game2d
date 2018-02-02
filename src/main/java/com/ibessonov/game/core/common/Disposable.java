package com.ibessonov.game.core.common;

/**
 * @author ibessonov
 */
public interface Disposable {

    boolean disposed();

    default void dispose() {}
}
