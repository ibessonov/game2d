package com.ibessonov.game;

import static com.google.inject.Guice.createInjector;

public class Main {

    public static void main(String args[]) {
        createInjector().getInstance(MainCanvas.class);
    }
}
