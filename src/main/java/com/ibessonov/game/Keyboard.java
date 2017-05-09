package com.ibessonov.game;

import javax.inject.Singleton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ibessonov
 */
@Singleton
public class Keyboard extends KeyAdapter {

    private final Set<Integer> pressedKeys = new HashSet<>();

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    public boolean isLeftPressed() {
        return pressedKeys.contains(KeyEvent.VK_LEFT);
    }

    public boolean isRightPressed() {
        return pressedKeys.contains(KeyEvent.VK_RIGHT);
    }

    public boolean isSpacePressed() {
        return pressedKeys.contains(KeyEvent.VK_SPACE);
    }
}
