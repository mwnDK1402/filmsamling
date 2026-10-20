package org.example.terminal;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;

/**
 * A terminal backed by the xterm terminal in the web page.
 *
 * <p>The browser cannot block, so {@link #readLine()} is compiled by TeaVM into
 * a coroutine: when no line is ready the calling code is suspended and control
 * returns to the page, which resumes it as soon as the user presses Enter.
 * Application code cannot tell the difference from {@link ConsoleTerminal}.
 *
 * <p>Input arrives through {@link WebInput}, which the page calls, and output
 * goes to the console stream that the page also renders.
 */
public final class WebTerminal implements Terminal {
    /**
     * Waits for the next submitted line, or {@code null} at end of input.
     * {@code @Async} makes this a coroutine rather than a real blocking call.
     */
    @Async
    @Override
    public native String readLine();

    private void readLine(AsyncCallback<String> callback) {
        WebInput.awaitLine(callback);
    }

    @Override
    public void println(String line) {
        System.out.println(line);
    }

    @Override
    public void print(String text) {
        System.out.print(text);
    }
}
