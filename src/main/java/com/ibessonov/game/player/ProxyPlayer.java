package com.ibessonov.game.player;

import java.util.function.Function;

public class ProxyPlayer extends BridgedPlayer {

    public ProxyPlayer(Player delegate) {
        super(delegate);
    }

    @Override
    public Player next() {
        delegate = delegate.next();
        return this;
    }

    public void transform(Function<Player, Player> function) {
        delegate = function.apply(delegate);
    }
}
