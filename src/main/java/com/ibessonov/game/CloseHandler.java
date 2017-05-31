package com.ibessonov.game;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class CloseHandler {

    @Inject
    private Provider<Game> gameProvider;

    public void handleClose() {
        // lazy injection to avoid loops
        gameProvider.get().stop();
    }
}
