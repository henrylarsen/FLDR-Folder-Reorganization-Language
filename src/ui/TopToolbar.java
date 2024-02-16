package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

/**
 * Represents the top toolbar, providing access to load/save scripts and editor options
 */
public class TopToolbar extends Toolbar {
    private static final String LOAD_ACTION = "load";
    private static final String SAVE_ACTION = "save";
    private static final String EDITOR = "editor";
    private static final String EXAMPLE = "example";
    private static final String STUDY_TASK = "study task";

    private MainPanel panel;

    public TopToolbar(MainPanel panel) {
        this.panel = panel;

        add(createScriptButton("Load Script", LOAD_ACTION));
        add(createButtonSpacer());
        add(createScriptButton("Save Script", SAVE_ACTION));
        add(createButtonSpacer());
        add(Box.createHorizontalGlue());
        add(createScriptButton("Script Editor", EDITOR));
        add(createButtonSpacer());
        add(createScriptButton("Example Syntax", EXAMPLE));
        add(createButtonSpacer());
        add(createScriptButton("Study Task", STUDY_TASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean load = e.getActionCommand().equals(LOAD_ACTION);
        boolean save = e.getActionCommand().equals(SAVE_ACTION);
        boolean edit = e.getActionCommand().equals(EDITOR);
        boolean example = e.getActionCommand().equals(EXAMPLE);
        boolean studyTask = e.getActionCommand().equals(STUDY_TASK);
        int selectMode = load ? FileDialog.LOAD : FileDialog.SAVE;
        File resultantFile = null;
        if (load || save ) {
            FileDialog selectDialog = createFileDialog(selectMode);
            resultantFile = getSelectedFile(selectDialog);
            if (resultantFile == null) {
                return;
            }
        }

        try {
            if (load) {
                panel.loadScript(resultantFile);
            } else if (save) {
                panel.saveScriptTo(resultantFile);
            } else if (edit) {
                panel.setMode(EditorMode.EDIT);
            } else if (example) {
                panel.setMode(EditorMode.EXAMPLE);
            } else if (studyTask) {
                panel.setMode(EditorMode.STUDY);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog (
                    this,
                    "An I/O error occurred",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private File getSelectedFile(FileDialog selectDialog) {
        File[] resultingFiles = selectDialog.getFiles();

        // canceled operation
        if (resultingFiles.length == 0) {
            return null;
        }

        if (resultingFiles.length > 1) {
            JOptionPane.showMessageDialog (
                    this,
                    "You cannot open multiple files!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        return resultingFiles[0];
    }

    private FileDialog createFileDialog(int selectMode) {
        JFrame topFrame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this);
        FileDialog selectDialog = new FileDialog(topFrame, "Select a Script File", selectMode);

        // TODO: actually agree on extension for script? FOL = File Organization Language
        selectDialog.setFilenameFilter((dir, name) -> name.endsWith(".fol"));
        selectDialog.setVisible(true);
        return selectDialog;
    }
}
