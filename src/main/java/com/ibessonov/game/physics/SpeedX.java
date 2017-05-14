package com.ibessonov.game.physics;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author ibessonov
 */
public class SpeedX {

    private int value;
    private int startingSpeed;
    private int acceleration;
    private int maxSpeed;
    private int scale;

    public SpeedX(int initialSpeed, int acceleration, int maxSpeed, int scale) {
        this.startingSpeed = initialSpeed;
        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
        this.scale = scale;
    }

    public void stop() {
        value = 0;
    }

    public int value() {
        return value / scale;
    }

    public void update(boolean moveLeft, boolean moveRight) {
        if (value == 0) {
            if (moveRight) {
                value = startingSpeed;
            } else if (moveLeft) {
                value = -startingSpeed;
            }
        } else if (value > 0) {
            if (moveRight) {
                value = min(maxSpeed, value + acceleration);
            } else {
                value = max(0, value - acceleration);
                if (moveLeft) {
                    value -= acceleration;
                }
            }
        } else {
            if (moveLeft) {
                value = max(-maxSpeed, value - acceleration);
            } else {
                value = min(0, value + acceleration);
                if (moveRight) {
                    value += acceleration;
                }
            }
        }
    }
}
