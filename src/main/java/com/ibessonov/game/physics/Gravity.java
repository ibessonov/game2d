package com.ibessonov.game.physics;

/**
 * @author ibessonov
 */
public class Gravity {

    private int acceleration;
    private int terminalVelocity;
    private int scale;
    private int direction;

    public Gravity(int acceleration, int terminalVelocity, int scale, boolean down) {
        this.acceleration = acceleration;
        this.terminalVelocity = terminalVelocity;
        this.scale = scale;
        this.direction = down ? 1 : -1;
    }

    public int directedSpeed(int speed) {
        return direction * speed * scale;
    }

    public int accelerate(int speed) {
        speed += direction * acceleration;
        return speed * direction <= terminalVelocity ? speed : terminalVelocity * direction;
    }

    public int scale(int speed) {
        return speed / scale;
    }

    public boolean isDown() {
        return direction > 0;
    }

    public void flip() {
        direction *= -1;
    }
}
