package com.ibessonov.game;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

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

    private final boolean windows = System.getProperty("os.name", "").toLowerCase().contains("windows");

    private static final int MASK_LEFT = 0x01;
    private static final int MASK_RIGHT = 0x02;
    private static final int MASK_DOWN = 0x04;
    private static final int MASK_UP = 0x08;
    private static final int MASK_JUMP = 0x10;
    private static final int MASK_FIRE = 0x20;
    private static final int MASK_START = 0x40;

    private Controller controller = null;
    private int mask;
    private int oldm;

    private final Set<Integer> pressedKeys = new HashSet<>();
    private final Set<Integer> tappedKeys = new HashSet<>();

    @Inject
    public void init(MainCanvas canvas) {
        canvas.addKeyListener(this);

        if (windows) {
            Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
            controller = ca[ca.length - 1];
//        for (Component component : controller.getComponents()) {
//            System.out.println(component.getIdentifier().getName());
//        }
        }
    }

    public void poll() {
        if (!windows) return;

        controller.poll();
        oldm = mask;

        mask = 0;
        for (Component component : controller.getComponents()) {
            int data = Math.round(component.getPollData());
            if (data != 0) {
                switch (component.getIdentifier().getName()) {
                    case "x":
                        if (data == -1) {
                            mask |= MASK_LEFT;
                        }
                        if (data == 1) {
                            mask |= MASK_RIGHT;
                        }
                        break;
                    case "y":
                        if (data == -1) {
                            mask |= MASK_UP;
                        }
                        if (data == 1) {
                            mask |= MASK_DOWN;
                        }
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
    public void keyPressed(KeyEvent e) {
        if (pressedKeys.add(e.getKeyCode()))
            tappedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    public boolean isLeftPressed() {
        return !isKeyPressed(VK_RIGHT) && isKeyPressed(VK_LEFT)
            || !isJoyPressed(MASK_RIGHT) && isJoyPressed(MASK_LEFT);
    }

    public boolean isRightPressed() {
        return !isKeyPressed(VK_LEFT) && isKeyPressed(VK_RIGHT)
            || !isJoyPressed(MASK_LEFT) && isJoyPressed(MASK_RIGHT);
    }

    public boolean isUpPressed() {
        return !isKeyPressed(VK_DOWN) && isKeyPressed(VK_UP)
            || !isJoyPressed(MASK_DOWN) && isJoyPressed(MASK_UP);
    }

    public boolean isDownPressed() {
        return !isKeyPressed(VK_UP) && isKeyPressed(VK_DOWN)
            || !isJoyPressed(MASK_UP) && isJoyPressed(MASK_DOWN);
    }

    public boolean isJumpPressed() {
        return isKeyPressed(VK_SPACE)
            || isJoyPressed(MASK_JUMP);
    }

    public boolean isJumpTapped() {
        return isKeyTapped(VK_SPACE)
            || isJoyTapped(MASK_JUMP);
    }

    public boolean isFireTapped() {
        return isKeyTapped(VK_CONTROL)
            || isJoyTapped(MASK_FIRE);
    }

    public boolean isFlipGravityTapped() {
        return isKeyTapped(VK_ENTER)
            || isJoyTapped(MASK_START);
    }

    public boolean isNightVisionKeyTapped() {
        return isKeyTapped(KeyEvent.VK_X);
    }

    private boolean isKeyTapped(int keyCode) {
        return tappedKeys.remove(keyCode);
    }

    private boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    private boolean isJoyPressed(int m) {
        return (mask & m) != 0;
    }

    private boolean isJoyTapped(int m) {
        return (mask & m) != 0 && (oldm & m) == 0;
    }
}
