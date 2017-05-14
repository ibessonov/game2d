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
    private final Set<Integer> tappedKeys = new HashSet<>();

    @Override
    public void keyPressed(KeyEvent e) {
        if (pressedKeys.add(e.getKeyCode()))
            tappedKeys.add(e.getKeyCode());
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

    public boolean isJumpPressed() {
        return pressedKeys.contains(KeyEvent.VK_SPACE);
    }

    public boolean isJumpTapped() {
        return tappedKeys.remove(KeyEvent.VK_SPACE);
    }

    public boolean isFireTapped() {
        return tappedKeys.remove(KeyEvent.VK_CONTROL);
    }

    public boolean isFlipGravityTapped() {
        return tappedKeys.remove(KeyEvent.VK_ENTER);
    }
}
