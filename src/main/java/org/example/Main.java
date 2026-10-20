package org.example;

import org.example.storage.DataDirectory;
import org.example.storage.FileStorage;
import org.example.terminal.ConsoleTerminal;

/**
 * The desktop program.
 *
 * <p>It selects the console and the CSV file, then hands both to the shared
 * {@link FilmSamlingApp}. There is one application, and this is the console
 * entry point to it.
 *
 * <p>The collection lives in the platform's per-user data directory rather
 * than next to the source, so it is not tied to the working directory and is
 * not lost when the project is cleaned or cloned again. See
 * {@link DataDirectory}.
 */
public class Main {
    public static void main(String[] args) {
        FileStorage storage = new FileStorage(DataDirectory.collectionFile().toString());
        new FilmSamlingApp(new ConsoleTerminal(), storage).run();
    }
}
