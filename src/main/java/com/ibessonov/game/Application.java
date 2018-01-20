package com.ibessonov.game;

public class Application {

    public static void main(String args[]) {
//        glfwPollEvents();
//        for (int i = GLFW_JOYSTICK_1; i <= GLFW_JOYSTICK_LAST; i++) {
//            System.out.println(i + ": " + glfwGetJoystickName(i));
//        }
//        System.out.println(glfwGetJoystickButtons(GLFW_JOYSTICK_1));

        new Game().start();
    }
}
