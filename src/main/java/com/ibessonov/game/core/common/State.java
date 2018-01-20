package com.ibessonov.game.core.common;

/**
 * @author ibessonov
 */
public interface State<S extends State<S>> {

    @SuppressWarnings("unchecked")
    default S next() {
        return (S) this;
    }
}
