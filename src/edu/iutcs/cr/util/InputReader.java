package edu.iutcs.cr.util;

import java.util.Scanner;


public class InputReader {

    private static InputReader instance;
    private final Scanner scanner;

    private InputReader() {
        scanner = new Scanner(System.in);
    }

    /** Returns (and lazily creates) the single shared instance. */
    public static InputReader getInstance() {
        if (instance == null) {
            instance = new InputReader();
        }
        return instance;
    }

    /** Reads the next full line of input. */
    public String nextLine() {
        return scanner.nextLine();
    }

    /**
     * Reads the next {@code int} and consumes the trailing newline so that
     * subsequent {@link #nextLine()} calls are not poisoned with an empty string.
     */
    public int nextInt() {
        int value = scanner.nextInt();
        scanner.nextLine(); // consume trailing newline
        return value;
    }

    /**
     * Reads the next {@code double} and consumes the trailing newline.
     */
    public double nextDouble() {
        double value = scanner.nextDouble();
        scanner.nextLine(); // consume trailing newline
        return value;
    }

    /**
     * Reads the next {@code boolean} and consumes the trailing newline.
     */
    public boolean nextBoolean() {
        boolean value = scanner.nextBoolean();
        scanner.nextLine(); // consume trailing newline
        return value;
    }

    /** Reads the next whitespace-delimited token. */
    public String next() {
        return scanner.next();
    }
}
