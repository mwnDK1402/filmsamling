package org.example.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A CSV file on disk: the desktop implementation of {@link Storage}.
 *
 * <p>Reads and writes with {@code java.io}, one record per line. The file is
 * created on first write, so the program can start with nothing on disk.
 *
 * <p>Errors are rethrown as {@link UncheckedIOException} rather than left
 * checked, so callers do not have to wrap every call in {@code try/catch}. This
 * is a prototype and the program is expected to stop if the file cannot be
 * read or written.
 */
public final class FileStorage implements Storage {
    private final File file;

    public FileStorage(String path) {
        this.file = new File(path);
    }

    @Override
    public List<String> readLines() {
        if (!file.exists()) {
            return new ArrayList<>();
        }
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + file, e);
        }
        return lines;
    }

    @Override
    public void writeLines(List<String> lines) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write " + file, e);
        }
    }

    @Override
    public boolean exists() {
        return file.exists() && file.length() > 0;
    }

    @Override
    public void clear() {
        writeLines(new ArrayList<>());
    }
}
