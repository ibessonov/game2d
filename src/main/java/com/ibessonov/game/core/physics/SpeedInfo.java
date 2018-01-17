package com.ibessonov.game.core.physics;

import com.ibessonov.game.core.geometry.SubPixel;

/**
 * @author ibessonov
 */
public class SpeedInfo {

    private final float startingSpeed;
    private final float acceleration;
    private final float maxSpeed;

    public SpeedInfo(float startingSpeed, float acceleration, float maxSpeed) {
        this.startingSpeed = startingSpeed;
        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
    }

    public void update(SubPixel speed, boolean moveLeft, boolean moveRight) {
        if (speed.floatValue() > 0) {
            if (moveRight) {
                speed.add(acceleration);
                if (speed.floatValue() > maxSpeed) {
                    speed.set(maxSpeed);
                }
            } else {
                speed.add(-acceleration);
                if (speed.floatValue() < 0) {
                    speed.set(0);
                }
                if (moveLeft) {
                    speed.add(-acceleration);
                }
            }
        } else if (speed.floatValue() < 0) {
            if (moveLeft) {
                speed.add(-acceleration);
                if (speed.floatValue() < -maxSpeed) {
                    speed.set(-maxSpeed);
                }
            } else {
                speed.add(acceleration);
                if (speed.floatValue() > 0) {
                    speed.set(0);
                }
                if (moveRight) {
                    speed.add(acceleration);
                }
            }
        } else {
            if (moveRight) {
                speed.set(startingSpeed);
            } else if (moveLeft) {
                speed.set(-startingSpeed);
            }
        }
    }
}
