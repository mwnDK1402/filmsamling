package org.example;

import org.example.terminal.ConsoleTerminal;

/**
 * The desktop program.
 *
 * <p>It selects the console implementation and hands it to the shared
 * {@link FilmSamlingApp}. There is one application, and this is the console
 * entry point to it.
 */
public class Main {
    public static void main(String[] args) {
        new FilmSamlingApp(new ConsoleTerminal()).run();
    }
}
