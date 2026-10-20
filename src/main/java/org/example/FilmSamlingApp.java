package org.example;

import java.util.List;
import org.example.storage.Storage;
import org.example.terminal.Terminal;

/**
 * The program itself, independent of any particular console or storage.
 *
 * <p>Everything here talks to the {@link Terminal} and {@link Storage}
 * interfaces, so this class is the same code on the desktop and in the browser.
 * The entry points only decide which implementations to hand it.
 *
 * <p>This is currently a prototype: it stores whatever the user types, one line
 * per entry, and lists it back. It exists to prove that the storage layer works
 * identically in both builds, before the real film model is built on top.
 */
public final class FilmSamlingApp {
    private final Terminal terminal;
    private final Storage storage;

    public FilmSamlingApp(Terminal terminal, Storage storage) {
        this.terminal = terminal;
        this.storage = storage;
    }

    /** Runs until the user enters an empty line. */
    public void run() {
        terminal.println("Filmsamling - prototype af lageret.");

        List<String> entries = storage.readLines();
        if (storage.exists()) {
            terminal.println("Indlæste " + entries.size() + " linjer fra lageret.");
        } else {
            terminal.println("Intet gemt endnu - der startes forfra.");
        }

        terminal.println("Skriv en linje og tryk Enter. Tom linje afslutter.");
        terminal.println();

        while (true) {
            String line = terminal.readLine();
            if (line == null || line.isBlank()) {
                break;
            }
            entries.add(line);
            storage.writeLines(entries);
            terminal.println("Gemt. (" + entries.size() + " linjer i alt)");
        }

        terminal.println();
        terminal.println("Indhold i lageret:");
        if (entries.isEmpty()) {
            terminal.println("  (tomt)");
        } else {
            for (String entry : storage.readLines()) {
                terminal.println("  " + entry);
            }
        }
        terminal.println("Farvel!");
    }
}
