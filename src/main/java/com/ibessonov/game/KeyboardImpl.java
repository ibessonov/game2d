package com.ibessonov.game;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import static java.awt.event.KeyEvent.*;

/**
 * @author ibessonov
 */
class KeyboardImpl implements Keyboard {

    private static final boolean windows = System.getProperty("os.name", "").toLowerCase().contains("windows");

    private static final int MASK_LEFT  = 0x01;
    private static final int MASK_RIGHT = 0x02;
    private static final int MASK_DOWN  = 0x04;
    private static final int MASK_UP    = 0x08;
    private static final int MASK_JUMP  = 0x10;
    private static final int MASK_FIRE  = 0x20;
    private static final int MASK_START = 0x40;

    private int mask;
    private int oldm;

    private final Set<Integer> pressedKeys = new ConcurrentSkipListSet<>();
    private Controller controller = null;

    public KeyboardImpl(Canvas canvas) {
        canvas.addKeyListener(new KeyboardAdapter()); // I don't like this line

        if (windows) {
            Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
            controller = ca[ca.length - 1];
//        for (Component component : controller.getComponents()) {
//            System.out.println(component.getIdentifier().getName());
//        }
        }
    }

    public void poll() {
        oldm = mask;
        mask = 0;

        if (pressedKeys.contains(VK_LEFT)) mask |= MASK_LEFT;
        if (pressedKeys.contains(VK_RIGHT)) mask |= MASK_RIGHT;
        if (pressedKeys.contains(VK_DOWN)) mask |= MASK_DOWN;
        if (pressedKeys.contains(VK_UP)) mask |= MASK_UP;
        if (pressedKeys.contains(VK_SPACE)) mask |= MASK_JUMP;
        if (pressedKeys.contains(VK_CONTROL)) mask |= MASK_FIRE;
        if (pressedKeys.contains(VK_ENTER)) mask |= MASK_START;

        if (!windows) return;

        controller.poll();
        for (Component component : controller.getComponents()) {
            int data = Math.round(component.getPollData());
            if (data != 0) {
                switch (component.getIdentifier().getName()) {
                    case "x":
                        if (data == -1) mask |= MASK_LEFT;
                        if (data == 1)  mask |= MASK_RIGHT;
                        break;
                    case "y":
                        if (data == -1) mask |= MASK_UP;
                        if (data == 1)  mask |= MASK_DOWN;
                        break;
                    case "2":
                        mask |= MASK_JUMP;
                        break;
                    case "3":
                        mask |= MASK_FIRE;
                        break;
                    case "11":
                        mask |= MASK_START;
                        break;
                    default:
                        System.out.println(component.getIdentifier().getName());
                }
            }
        }
    }

    @Override
    public boolean isLeftPressed() {
        return !pressed(MASK_RIGHT) && pressed(MASK_LEFT);
    }

    @Override
    public boolean isRightPressed() {
        return !pressed(MASK_LEFT) && pressed(MASK_RIGHT);
    }

    @Override
    public boolean isUpPressed() {
        return !pressed(MASK_DOWN) && pressed(MASK_UP);
    }

    @Override
    public boolean isUpTapped() {
        return tapped(MASK_UP);
    }

    @Override
    public boolean isDownPressed() {
        return !pressed(MASK_UP) && pressed(MASK_DOWN);
    }

    @Override
    public boolean isDownTapped() {
        return tapped(MASK_DOWN);
    }

    @Override
    public boolean isJumpPressed() {
        return pressed(MASK_JUMP);
    }

    @Override
    public boolean isJumpTapped() {
        return tapped(MASK_JUMP);
    }

    @Override
    public boolean isFireTapped() {
        return tapped(MASK_FIRE);
    }

    @Override
    public boolean isStartTapped() {
        return tapped(MASK_START);
    }

    private boolean pressed(int m) {
        return (mask & m) != 0;
    }

    private boolean tapped(int m) {
        return (mask & m) != 0 && (oldm & m) == 0;
    }

    private class KeyboardAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            pressedKeys.add(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            pressedKeys.remove(e.getKeyCode());
        }
    }
}
