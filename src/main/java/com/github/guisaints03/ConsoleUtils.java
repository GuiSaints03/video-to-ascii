package com.github.guisaints03;

public class ConsoleUtils {

    private ConsoleUtils() {}

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
