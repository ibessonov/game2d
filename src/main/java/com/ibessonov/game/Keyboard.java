package com.ibessonov.game;

import javax.inject.Inject;
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

    @Inject
    public void init(MainCanvas canvas) {
        canvas.addKeyListener(this);
    }

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
        return isKeyPressed(KeyEvent.VK_LEFT);
    }

    public boolean isRightPressed() {
        return isKeyPressed(KeyEvent.VK_RIGHT);
    }

    public boolean isJumpPressed() {
        return isKeyPressed(KeyEvent.VK_SPACE);
    }

    public boolean isJumpTapped() {
        return isKeyTapped(KeyEvent.VK_SPACE);
    }

    public boolean isFireTapped() {
        return isKeyTapped(KeyEvent.VK_CONTROL);
    }

    public boolean isFlipGravityTapped() {
        return isKeyTapped(KeyEvent.VK_ENTER);
    }

    public boolean isKeyTapped(int keyCode) {
        return tappedKeys.remove(keyCode);
    }

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
}
