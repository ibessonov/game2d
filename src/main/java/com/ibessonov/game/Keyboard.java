package com.ibessonov.game;

/**
 * @author ibessonov
 */
public interface Keyboard {

    boolean isLeftPressed();
    boolean isLeftTapped();

    boolean isRightPressed();
    boolean isRightTapped();

    boolean isUpPressed();
    boolean isUpTapped();

    boolean isDownPressed();
    boolean isDownTapped();

    boolean isJumpPressed();
    boolean isJumpTapped();

    boolean isFirePressed();
    boolean isFireTapped();

    boolean isStartTapped();
}
