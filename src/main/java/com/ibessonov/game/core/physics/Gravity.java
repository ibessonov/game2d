package com.ibessonov.game.core.physics;

import com.ibessonov.game.core.geometry.SubPixel;

import static com.ibessonov.game.core.physics.YDirection.DOWN;
import static com.ibessonov.game.core.physics.YDirection.UP;

/**
 * @author ibessonov
 */
public class Gravity {

    private final float acceleration;
    private final float terminalVelocity;
    private YDirection direction;

    public Gravity(float acceleration, float terminalVelocity, YDirection direction) {
        this.acceleration = acceleration;
        this.terminalVelocity = terminalVelocity;
        this.direction = direction;
    }

    public float directedSpeed(float absoluteSpeed) {
        return direction.sign * absoluteSpeed;
    }

    public void accelerate(SubPixel speed) {
        int sign = direction.sign;
        speed.add(acceleration * sign);
        if (speed.floatValue() * sign > terminalVelocity) {
            speed.set(sign * terminalVelocity);
        }
    }

    public YDirection yDirection() {
        return direction;
    }

    public void flip() {
        direction = (direction == DOWN) ? UP : DOWN;
    }
}
