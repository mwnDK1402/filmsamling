package org.example.terminal;

import java.util.Scanner;

/**
 * The ordinary terminal: a real console on the desktop program.
 *
 * <p>This is a thin wrapper over {@code System.in} and {@code System.out}, and
 * it is where {@link Scanner} belongs. Application code never sees any of that,
 * so the browser build — where {@code Scanner} does not exist — never has to
 * compile this class.
 */
public final class ConsoleTerminal implements Terminal {
    private final Scanner in = new Scanner(System.in);

    @Override
    public String readLine() {
        if (!in.hasNextLine()) {
            return null;
        }
        return in.nextLine();
    }

    @Override
    public void println(String line) {
        System.out.println(line);
    }

    @Override
    public void print(String text) {
        System.out.print(text);
    }
}
