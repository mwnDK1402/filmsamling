package org.example;

import org.teavm.jso.JSExport;

/**
 * Browser entry point, compiled by TeaVM to WebAssembly.
 *
 * <p>A WebAssembly module runs on the browser's single JavaScript thread and
 * therefore cannot block waiting for a keystroke. The program is driven from
 * JavaScript instead:
 *
 * <ul>
 *   <li>{@link #start()} prints the banner and runs the loop until it needs input;</li>
 *   <li>each completed terminal line is handed to {@link #pushLine(String)},
 *       which resumes the loop.</li>
 * </ul>
 *
 * <p>This mirrors the behaviour of the console {@code Main} echo program while
 * remaining non-blocking.
 */
public final class BrowserMain {
    private static final java.util.ArrayDeque<String> pendingLines = new java.util.ArrayDeque<>();
    private static boolean waitingForInput;
    private static boolean finished;

    private BrowserMain() {
    }

    /** TeaVM entry point: prints the banner and starts the input loop. */
    public static void main(String[] args) {
        start();
    }

    /** Called by JavaScript once the terminal is wired up. Idempotent. */
    @JSExport
    public static void start() {
        if (finished) {
            return;
        }
        System.out.println("This program echoes your input, in order to test whether IO works.");
        System.out.println("(Type lines; an empty line exits)");
        resume();
    }

    /** Called by JavaScript with each completed line typed in the terminal. */
    @JSExport
    public static void pushLine(String line) {
        if (line == null || finished) {
            return;
        }
        pendingLines.addLast(line);
        if (waitingForInput) {
            resume();
        }
    }

    private static void resume() {
        waitingForInput = false;
        while (!finished) {
            String line = pendingLines.pollFirst();
            if (line == null) {
                // Nothing to read: yield control back to JavaScript.
                waitingForInput = true;
                return;
            }
            if (line.trim().isEmpty()) {
                System.out.println("Goodbye!");
                finished = true;
                return;
            }
            System.out.println(line);
        }
    }
}
