package org.example.storage;

import java.util.ArrayList;
import java.util.List;

/**
 * The browser's {@code localStorage}: the browser implementation of
 * {@link Storage}.
 *
 * <p>Why not just write a file? TeaVM <em>does</em> provide {@code java.io.File}
 * in the browser, so a {@link FileStorage} would appear to work — but it is
 * backed by an in-memory file system that is discarded the moment the page is
 * reloaded. The collection would silently vanish. {@code localStorage} is the
 * only browser facility here that actually survives, so the collection is kept
 * there instead.
 *
 * <p>The whole CSV text is stored under a single key. Reading splits it into
 * lines in Java, so the exact text is never reinterpreted by JavaScript.
 *
 * <p>TeaVM's storage type is written out in full, because this class also
 * implements a project interface named {@code Storage}.
 */
public final class LocalStorageStorage implements Storage {
    /** Separates records inside the stored text. Same convention as the file. */
    private static final String LINE_SEPARATOR = "\n";

    private final String key;

    public LocalStorageStorage(String key) {
        this.key = key;
    }

    @Override
    public List<String> readLines() {
        String text = org.teavm.jso.browser.Storage.getLocalStorage().getItem(key);
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return lines;
        }
        // Split by hand rather than with String.split, so a trailing separator
        // does not produce a spurious empty last record.
        int start = 0;
        while (start <= text.length()) {
            int end = text.indexOf('\n', start);
            String line;
            if (end < 0) {
                line = text.substring(start);
            } else {
                line = text.substring(start, end);
            }
            if (line.endsWith("\r")) {
                line = line.substring(0, line.length() - 1);
            }
            if (!line.isEmpty()) {
                lines.add(line);
            }
            if (end < 0) {
                break;
            }
            start = end + 1;
        }
        return lines;
    }

    @Override
    public void writeLines(List<String> lines) {
        StringBuilder text = new StringBuilder();
        for (String line : lines) {
            text.append(line).append(LINE_SEPARATOR);
        }
        org.teavm.jso.browser.Storage.getLocalStorage().setItem(key, text.toString());
    }

    @Override
    public boolean exists() {
        String text = org.teavm.jso.browser.Storage.getLocalStorage().getItem(key);
        return text != null && !text.isEmpty();
    }

    @Override
    public void clear() {
        org.teavm.jso.browser.Storage.getLocalStorage().removeItem(key);
    }
}
