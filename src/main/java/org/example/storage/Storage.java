package org.example.storage;

import java.util.List;

/**
 * Where the film collection is kept between runs.
 *
 * <p>The application reads and writes only through this interface, so the same
 * code runs on a real file and in a browser. Nothing above this layer mentions
 * {@code File}, {@code Path} or {@code localStorage}; those are details of one
 * particular implementation.
 *
 * <p>Two implementations exist:
 * <ul>
 *   <li>{@link FileStorage} — a CSV file on disk, used by the desktop program.</li>
 *   <li>{@link LocalStorageStorage} — the browser's {@code localStorage},
 *       used by the browser build.</li>
 * </ul>
 *
 * <p>The exchange format is <em>lines of text</em>, one CSV record per line. The
 * interface deliberately says nothing about how a line is split into fields:
 * that is the collection's job, and it stays in the application so both builds
 * parse identically.
 */
public interface Storage {
    /**
     * Returns everything previously stored, one line per record, in the order
     * it was written. Returns an empty list when nothing has been stored yet.
     *
     * <p>Never returns {@code null}: "no collection yet" and "empty collection"
     * are the same thing to a caller.
     */
    List<String> readLines();

    /**
     * Replaces the stored contents with these lines.
     *
     * <p>It is a whole-file replacement rather than an append, because the
     * collection is small and always written out in full. That keeps the two
     * implementations identical in behaviour.
     */
    void writeLines(List<String> lines);

    /**
     * Whether anything has been stored yet.
     *
     * <p>Lets the program tell "no collection yet" from "a collection I have
     * emptied", which matters when deciding whether to load a sample set.
     */
    boolean exists();

    /** Removes the stored contents entirely. */
    void clear();
}
