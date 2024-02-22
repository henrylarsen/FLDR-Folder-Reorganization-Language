package ui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Collection;
import java.util.Collections;


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
        setPaths("", Collections.emptyList());
    }

    /**
     * Represents the paths to display in the panel. Fully resolved to include all necessary parents
     *
     * @param paths All the paths to display, including parents.
     */
    public void setPaths(String targetDirectory, Collection<Path> paths) {
        // convert absolute paths to relative paths for displaying in the tree neatly
        List<Path> relativePaths = paths.stream().map((path ->
                Paths.get(path.toString().substring(targetDirectory.length())))
        ).toList();

        DefaultTreeModel model = (DefaultTreeModel) getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.setUserObject(targetDirectory);
        // clear previous children
        root.removeAllChildren();
        for (Path part : relativePaths) {
            DefaultMutableTreeNode parent = root;
            // create nodes for each directory until it reaches end of path
            for (Path sub : part) {
                DefaultMutableTreeNode child = getChildNode(parent, sub.toString());
                // if child does not exist create the node and continue
                if (child == null) {
                    child = new DefaultMutableTreeNode(sub.toString());
                    parent.add(child);
                }
                parent = child;
            }
        }
        model.reload();
    }

    // returns the child node if it exists, if not return null
    private DefaultMutableTreeNode getChildNode(DefaultMutableTreeNode node, String childName) {
        int count = node.getChildCount();
        for (int i = 0; i < count; i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            if (child.getUserObject().toString().equals(childName)) {
                return child;
            }
        }
        return null;
    }
}
