package com.ibessonov.game.core.physics;

import com.ibessonov.game.core.geometry.SubPixel;

/**
 * @author ibessonov
 */
public class Gravity {

    private final float acceleration;
    private final float terminalVelocity;
    private int direction;

    public Gravity(float acceleration, float terminalVelocity, boolean down) {
        this.acceleration = acceleration;
        this.terminalVelocity = terminalVelocity;
        this.direction = down ? 1 : -1;
    }

    public float directedSpeed(float speed) {
        return direction * speed;
    }

    public void accelerate(SubPixel speed) {
        speed.add(acceleration * direction);
        if (speed.floatValue() * direction > terminalVelocity) {
            speed.set(direction * terminalVelocity);
        }
    }

    public boolean isDown() {
        return direction > 0;
    }

    public void flip() {
        direction *= -1;
    }
}
