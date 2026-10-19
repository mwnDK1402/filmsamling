package org.example;

/**
 * The application, running in the browser.
 *
 * <p>This is the same program as the console entry point: an ordinary blocking
 * read loop. {@link Terminal#readLine()} suspends and resumes under the hood
 * (TeaVM coroutines) but looks like normal blocking I/O, so no control flow is
 * inverted and no state machine is needed.
 */
public final class BrowserMain {
    private BrowserMain() {
    }

    public static void main(String[] args) {
        System.out.println("This program echoes your input, in order to test whether IO works.");
        System.out.println("(Type lines; an empty line exits)");

        while (true) {
            String line = Terminal.readLine();
            if (line == null) {
                break;
            }
            if (line.trim().isEmpty()) {
                break;
            }
            System.out.println(line);
        }

        System.out.println("Goodbye!");
    }
}
