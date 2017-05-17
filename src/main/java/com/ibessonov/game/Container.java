package com.ibessonov.game;

import java.util.function.Consumer;

/**
 * @author ibessonov
 */
public interface Container<T> {

    void forEach(Consumer<T> c);
}
