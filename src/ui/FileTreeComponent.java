package ui;

import javax.swing.*;
import java.nio.file.Path;
import java.util.List;

/**
 * Represents a file tree to display, using a list of files
 *
 * Directory 1
 *   - Directory 2
 *      - File 2
 *   - Directory 3
 *      - File 5
 *   - File 2
 *
 *
 */
public class FileTreeComponent extends JTree {

    public FileTreeComponent() {
    }

    /**
     * Represents the paths to display in the panel. Fully resolved to include all necessary parents
     *
     * @param paths All the paths to display, including parents.
     */
    public void setPaths(List<Path> paths) {
        // TODO actually render paths on this component
    }
}
