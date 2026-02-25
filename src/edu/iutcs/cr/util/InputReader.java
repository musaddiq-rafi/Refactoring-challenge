package edu.iutcs.cr.util;

import java.util.Scanner;


public class InputReader {

    private static InputReader instance;
    private final Scanner scanner;

    private InputReader() {
        scanner = new Scanner(System.in);
    }

    public static InputReader getInstance() {
        if (instance == null) {
            instance = new InputReader();
        }
        return instance;
    }

    public String nextLine() {
        return scanner.nextLine();
    }

    public int nextInt() {
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    public double nextDouble() {
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }

    public boolean nextBoolean() {
        boolean value = scanner.nextBoolean();
        scanner.nextLine();
        return value;
    }

    public String next() {
        return scanner.next();
    }
}
