package org.example;

import org.example.terminal.Terminal;

/**
 * The program itself, independent of any particular console.
 *
 * <p>Everything here talks to the {@link Terminal} interface, so this class is
 * the same code on the desktop and in the browser. The entry points only decide
 * which implementation to hand it.
 */
public final class FilmSamlingApp {
    private final Terminal terminal;

    public FilmSamlingApp(Terminal terminal) {
        this.terminal = terminal;
    }

    /** Runs until the user enters an empty line. */
    public void run() {
        terminal.println("This program echoes your input, in order to test whether IO works.");

        while (true) {
            String line = terminal.readLine();
            if (line == null || line.isBlank()) {
                break;
            }
            terminal.println(line);
        }

        terminal.println("Goodbye!");
    }
}
