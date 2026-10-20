package org.example;

import org.example.terminal.Terminal;
import org.example.terminal.WebInput;
import org.example.terminal.WebTerminal;

/**
 * Entry point for the browser build.
 *
 * <p>It is the counterpart of {@link Main}: it selects the browser terminal
 * instead of the console one and runs the very same {@link FilmSamlingApp}.
 * Nothing about the application changes between the two.
 *
 * <p>The reference to {@link WebInput} keeps the page's input entry points in
 * the compiled program; they are unreachable in Java alone, because the page
 * calls them from outside.
 */
public final class BrowserMain {
    private BrowserMain() {
    }

    public static void main(String[] args) {
        // Keep WebInput's page-facing methods in the compiled program.
        if (args != null && args.length < 0) {
            WebInput.closeInput();
        }

        Terminal terminal = new WebTerminal();
        new FilmSamlingApp(terminal).run();
    }
}
