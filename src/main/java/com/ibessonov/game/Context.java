package com.ibessonov.game;

import com.google.inject.Injector;

import static com.google.inject.Guice.createInjector;

/**
 * @author ibessonov
 */
public class Context {

    public static Injector injector() {
        return InjectorHolder.INJECTOR;
    }

    public static <T> T inject(T object) {
        injector().injectMembers(object);
        return object;
    }

    private static class InjectorHolder {
        private static final Injector INJECTOR = createInjector();
    }
}
