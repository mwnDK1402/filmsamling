package org.example.storage;

import java.io.File;
import java.nio.file.Path;

/**
 * Where the desktop program keeps its data.
 *
 * <p>Runtime data does not belong in the project directory: the collection is
 * the user's, not the source tree's, and it should survive {@code mvn clean}
 * and a fresh clone. It is therefore placed in the per-user application data
 * directory for the current operating system:
 *
 * <ul>
 *   <li><b>Linux and other Unix</b> — {@code $XDG_DATA_HOME/filmsamling}, or
 *       {@code ~/.local/share/filmsamling} when {@code XDG_DATA_HOME} is unset.
 *       This is the location the freedesktop.org base directory specification
 *       defines for exactly this kind of file.</li>
 *   <li><b>Windows</b> — {@code %APPDATA%\filmsamling}, i.e.
 *       {@code C:\Users\<user>\AppData\Roaming\filmsamling}.</li>
 *   <li><b>macOS</b> — {@code ~/Library/Application Support/filmsamling}.</li>
 * </ul>
 *
 * <p>The choice is made by inspecting the operating system name, because the
 * Java standard library has no single call for "the application data directory
 * of this platform".
 *
 * <p>This class is desktop-only. The browser build never touches the file
 * system, so it never references this; TeaVM only compiles what
 * {@code BrowserMain} can reach, and this is not on that path.
 */
public final class DataDirectory {
    /** The directory name under the platform's data root. */
    private static final String APP_DIR = "filmsamling";

    /** The collection file, inside {@link #path()}. */
    private static final String FILE_NAME = "films.csv";

    private DataDirectory() {
    }

    /**
     * The application's data directory, as an absolute path. The directory is
     * <em>not</em> created here; {@link FileStorage} makes it when it first
     * writes, so merely starting the program does not litter the disk.
     */
    public static Path path() {
        return dataRoot().resolve(APP_DIR);
    }

    /** The full path of the collection file. */
    public static Path collectionFile() {
        return path().resolve(FILE_NAME);
    }

    /** The platform's root directory for user application data. */
    private static Path dataRoot() {
        String os = System.getProperty("os.name", "").toLowerCase();

        if (os.contains("win")) {
            String appData = System.getenv("APPDATA");
            if (appData != null && !appData.isBlank()) {
                return new File(appData).toPath();
            }
            return new File(System.getProperty("user.home"), "AppData/Roaming").toPath();
        }

        if (os.contains("mac")) {
            return new File(System.getProperty("user.home"), "Library/Application Support").toPath();
        }

        // Linux and the other Unixes: follow the XDG base directory spec.
        String xdgDataHome = System.getenv("XDG_DATA_HOME");
        if (xdgDataHome != null && !xdgDataHome.isBlank()) {
            return new File(xdgDataHome).toPath();
        }
        return new File(System.getProperty("user.home"), ".local/share").toPath();
    }
}
