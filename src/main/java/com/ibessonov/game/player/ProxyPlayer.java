package com.ibessonov.game.player;

import java.util.function.UnaryOperator;

public class ProxyPlayer extends BridgedPlayer {

    public ProxyPlayer(Player delegate) {
        super(delegate);
    }

    @Override
    public Player next() {
        delegate = delegate.next();
        return this;
    }

    public void transform(UnaryOperator<Player> function) {
        delegate = function.apply(delegate);
    }
}
