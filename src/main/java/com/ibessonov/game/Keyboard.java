package com.ibessonov.game;

/**
 * @author ibessonov
 */
public interface Keyboard {

    boolean isLeftPressed();
    boolean isRightPressed();
    boolean isUpPressed();
    boolean isDownPressed();

    boolean isJumpPressed();
    boolean isJumpTapped();

    boolean isFireTapped();

    boolean isStartTapped();
}
