package org.example;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSExport;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

/**
 * Terminal I/O that looks blocking to the rest of the program.
 *
 * <p>{@link #readLine()} is declared {@code @Async}: TeaVM compiles it as a
 * coroutine, suspending the calling Java code until JavaScript delivers a line.
 * From the caller's point of view it is an ordinary blocking method, so normal
 * Java control flow (loops, {@code Scanner}-style reading) works unchanged.
 *
 * <p>On the desktop this class is unused; the browser build replaces it with
 * real stdin/stdout.
 */
public final class Terminal {
    private Terminal() {
    }

    /** One waiting reader at a time — the app reads input strictly sequentially. */
    private static AsyncCallback<String> pendingReader;
    private static String deliveredLine;

    /** Called by JavaScript for every line the user types. */
    @JSExport
    public static void deliverLine(String line) {
        if (pendingReader == null) {
            // No read in progress: buffer the line until one is.
            deliveredLine = line;
            return;
        }
        AsyncCallback<String> reader = pendingReader;
        pendingReader = null;
        reader.complete(line);
    }

    /** Signals end of input, so a waiting read can finish. */
    @JSExport
    public static void closeInput() {
        if (pendingReader != null) {
            AsyncCallback<String> reader = pendingReader;
            pendingReader = null;
            reader.complete(null);
        } else {
            inputClosed = true;
        }
    }

    private static boolean inputClosed;

    /**
     * Reads one line, blocking (logically) until JavaScript supplies it.
     * Returns {@code null} at end of input.
     */
    @Async
    public static native String readLine();

    private static void readLine(AsyncCallback<String> callback) {
        if (deliveredLine != null) {
            String line = deliveredLine;
            deliveredLine = null;
            callback.complete(line);
            return;
        }
        if (inputClosed) {
            callback.complete(null);
            return;
        }
        pendingReader = callback;
        requestInput();
    }

    /**
     * Tells the browser a read is outstanding. The page uses this to stop
     * buffering typed input and start handing lines to {@link #deliverLine}.
     */
    @JSBody(script = "if (typeof window !== 'undefined' && window.teavmAwaitInput) { window.teavmAwaitInput(); }")
    private static native void requestInput();
}
