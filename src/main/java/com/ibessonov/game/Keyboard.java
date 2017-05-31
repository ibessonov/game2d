package com.ibessonov.game;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import static java.awt.event.KeyEvent.*;

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
        return isKeyPressed(VK_LEFT);
    }

    public boolean isRightPressed() {
        return isKeyPressed(VK_RIGHT);
    }

    public boolean isUpPressed() {
        return isKeyPressed(VK_UP);
    }

    public boolean isDownPressed() {
        return isKeyPressed(VK_DOWN);
    }

    public boolean isJumpPressed() {
        return isKeyPressed(VK_SPACE);
    }

    public boolean isJumpTapped() {
        return isKeyTapped(VK_SPACE);
    }

    public boolean isFireTapped() {
        return isKeyTapped(VK_CONTROL);
    }

    public boolean isFlipGravityTapped() {
        return isKeyTapped(VK_ENTER);
    }

    public boolean isKeyTapped(int keyCode) {
        return tappedKeys.remove(keyCode);
    }

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
}
