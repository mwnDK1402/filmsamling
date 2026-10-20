package org.example.terminal;

/**
 * The console the program talks to.
 *
 * <p>Application code reads and writes only through this interface, so the same
 * code runs on a real console and in a browser. Nothing above this layer refers
 * to {@code System.in}, {@code System.out} or {@code Scanner}; those are details
 * of one particular implementation.
 *
 * <p>Two implementations exist: {@link ConsoleTerminal}, used by the desktop
 * program, and the browser one, which drives an xterm terminal in the page.
 */
public interface Terminal {
    /**
     * Waits for the user to submit a line and returns it without the line
     * ending. Returns {@code null} once no more input will arrive.
     *
     * <p>This call blocks, as far as the caller is concerned. In the browser
     * that is arranged by compiling this method into a coroutine, so it
     * suspends instead of blocking the page.
     */
    String readLine();

    /** Writes a line of text, followed by a line break. */
    void println(String line);

    /** Writes a line of text with no line break. */
    void print(String text);

    /** Writes a line break. */
    default void println() {
        println("");
    }

    /**
     * Writes formatted text with no line break, using the same format strings
     * as {@link String#format}.
     *
     * <p>Note for the browser build: formatting is the one expensive feature in
     * the terminal layer. The first use of this method pulls in TeaVM's
     * {@code Formatter} support and enlarges the WebAssembly download
     * considerably, so reach for it deliberately rather than by default.
     *
     * <p>It is also less complete there than on the desktop. Conversions the
     * browser build does <em>not</em> support are {@code %n}, {@code %e} and
     * {@code %t}; each throws {@code UnknownFormatConversionException} at
     * runtime instead of during the build. Use {@code \n} rather than
     * {@code %n}, and avoid the other two.
     */
    default void printf(String format, Object... args) {
        print(String.format(format, args));
    }
}
