package com.ibessonov.game.physics;

/**
 * @author ibessonov
 */
public class Gravity {

    private int acceleration;
    private int terminalVelocity;
    private int direction;
    public static final int SCALE = 8;

    public Gravity(float acceleration, float terminalVelocity, boolean down) {
        this.acceleration = fromFloat(acceleration);
        this.terminalVelocity = fromFloat(terminalVelocity);
        this.direction = down ? 1 : -1;
    }

    public float directedSpeed(float speed) {
        return direction * speed;
    }

    public float accelerate(float speed) {
        int speedInt = fromFloat(speed);
        speedInt += direction * acceleration;
        return fromInt(speedInt * direction <= terminalVelocity ? speedInt : terminalVelocity * direction);
    }

    public boolean isDown() {
        return direction > 0;
    }

    public void flip() {
        direction *= -1;
    }

    public static int fromFloat(float speed) {
        return Math.round(speed * SCALE);
    }

    public static  float fromInt(int speed) {
        return 1f * speed / SCALE;
    }

    public static int normalize(int a) {
        return fromFloat(fromInt(a));
    }
}
