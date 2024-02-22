package ui;

import ast.Program;
import libs.ProgramScope;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

/**
 * Using a border layout to place the main areas of the application:
 *
 * |                      Top Toolbar                    |
 * | File Tree (Start) | Script Editor | File Tree (End) |
 * |                     Bottom Toolbar                  |
 *
 */
public class MainPanel extends JPanel {
    public static final int PREFERRED_WIDTH = 1200;
    public static final int PREFERRED_HEIGHT = 800;

    private final FileTreeComponent beforeScriptPreview = new FileTreeComponent();
    private final FileTreeComponent afterScriptPreview = new FileTreeComponent();
    private final ScriptEditorComponent scriptEditor = new ScriptEditorComponent();
    private final InfoDialog infoDialog = new InfoDialog("FOL, Explained");

    private final DSLRunner dslRunner = new DSLRunner();


    public MainPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

        add(new TopToolbar(this), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(new BottomToolbar(this), BorderLayout.SOUTH);
    }

    private JSplitPane createCenterPanel() {
        // Allow users to resize the panels to their liking...
        // little hack: split pane only allows two components, but we can nest them:
        // [[FileTree, ScriptEditor], FileTree]
        JSplitPane leftSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, beforeScriptPreview, scriptEditor);
        JSplitPane fullSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplitPane, afterScriptPreview);

        // size for a 1 : 2 : 1 ratio for left file tree, script editor, and right file tree
        leftSplitPane.setDividerLocation(PREFERRED_WIDTH / 4);
        fullSplitPanel.setDividerLocation(PREFERRED_WIDTH * 3 / 4);

        return fullSplitPanel;
    }

    public void loadScript(File file) throws IOException {
        scriptEditor.setScript(Files.readString(file.toPath()));
    }

    public void saveScriptTo(File file) throws IOException {
        Files.writeString (
                file.toPath(),
                scriptEditor.getScript(),
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE_NEW
        );
    }

    public void setMode(EditorMode mode) {
        scriptEditor.setMode(mode);
    }

    public void showDocs() {
        infoDialog.setVisible(true);
    }

    public void previewScript() {
        useScript(false);
    }

    public void executeScript() {
        useScript(true);
    }

    public void useScript(boolean move) {
        Program program;
        Map<Path, Path> pathMappings;

        try {
            program = dslRunner.getProgram(scriptEditor.getScript());
            pathMappings = program.evaluate(new ProgramScope());
        } catch (Throwable ex) {
            JOptionPane.showMessageDialog (
                    this,
                    "An error occurred: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
            return;
        }

        beforeScriptPreview.setPaths(program.getTargetDirectory(), pathMappings.keySet());
        afterScriptPreview.setPaths(program.getTargetDirectory(), pathMappings.values());

        if (move) {
            program.moveFiles(pathMappings);
        }
    }
}
