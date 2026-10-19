package org.example;

import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSExport;

/**
 * Line input from the web page, on the Java side.
 *
 * <p>The page calls {@link #deliverLine} once for each line the user submits;
 * {@link org.example.terminal.WebTerminal} waits for those lines through
 * {@link #awaitLine}, parking a coroutine callback until one arrives.
 *
 * <p>Together with the page's bridge this is the only code in the project that
 * knows a browser exists, and it is reached only through the browser build.
 */
public final class WebInput {
    private WebInput() {
    }

    /** Submitted lines that have not been read yet, in order. */
    private static final java.util.ArrayDeque<String> lines = new java.util.ArrayDeque<>();

    /** The read that is waiting for a line, if any. */
    private static AsyncCallback<String> waiter;

    /** True once no more input will arrive. */
    private static boolean closed;

    /**
     * Hands one submitted line to the program. Called by the page when the user
     * presses Enter.
     */
    @JSExport
    public static void deliverLine(String line) {
        lines.addLast(line == null ? "" : line);
        resume();
    }

    /** Signals that no further input will arrive. */
    @JSExport
    public static void closeInput() {
        closed = true;
        resume();
    }

    /**
     * Supplies the next submitted line to {@code callback}, parking it until
     * one is available. Completes with {@code null} at end of input.
     */
    public static void awaitLine(AsyncCallback<String> callback) {
        if (!lines.isEmpty()) {
            callback.complete(lines.removeFirst());
            return;
        }
        if (closed) {
            callback.complete(null);
            return;
        }
        waiter = callback;
        requestInput();
    }

    /** Completes a parked read if a line is now available, or input has ended. */
    private static void resume() {
        if (waiter == null) {
            return;
        }
        if (lines.isEmpty() && !closed) {
            return;   // nothing to hand over yet; keep waiting
        }
        AsyncCallback<String> w = waiter;
        waiter = null;
        w.complete(lines.isEmpty() ? null : lines.removeFirst());
    }

    /** Tells the page that the program is waiting for a line. */
    @JSBody(script = "if (typeof window !== 'undefined' && window.teavmAwaitInput) { window.teavmAwaitInput(); }")
    private static native void requestInput();
}
