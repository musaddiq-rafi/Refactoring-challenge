package edu.iutcs.cr.util;

import java.util.Scanner;

/**
 * Singleton wrapper around a single {@link Scanner} pointed at {@code System.in}.
 *
 * <p><strong>Why this exists:</strong> The original code created a {@code new Scanner(System.in)}
 * inside almost every method and, in a few places, called {@code scanner.close()}.
 * Closing a {@code Scanner} that wraps {@code System.in} closes the underlying stream
 * permanently &mdash; subsequent reads on any <em>other</em> Scanner will then throw
 * {@link java.util.NoSuchElementException}.  By funnelling all console I/O through
 * one shared instance this class eliminates:
 * <ul>
 *   <li>The resource-leak of repeatedly newing up Scanners without closing them.</li>
 *   <li>The accidental closure of {@code System.in} (notably {@code Hatchback.setCompact()}
 *       called {@code scanner.close()}).</li>
 * </ul>
 *
 * <p><strong>Design Pattern:</strong> Singleton
 *
 * @author refactored
 */
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
