package org.example;

import org.example.storage.FileStorage;
import org.example.terminal.ConsoleTerminal;

/**
 * The desktop program.
 *
 * <p>It selects the console and the CSV file, then hands both to the shared
 * {@link FilmSamlingApp}. There is one application, and this is the console
 * entry point to it.
 */
public class Main {
    /** Where the desktop build keeps the collection. */
    private static final String CSV_PATH = "films.csv";

    public static void main(String[] args) {
        new FilmSamlingApp(new ConsoleTerminal(), new FileStorage(CSV_PATH)).run();
    }
}
