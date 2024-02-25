package ui;

import ui.lib.TextLineNumber;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScriptEditorComponent extends JScrollPane {
    private JTextPane textPanel = new JTextPane();
    private boolean editMode;
    private String userScript;
    private String exampleScript;
    private final String scriptPath = "./src/ui/documentation";

    public ScriptEditorComponent() {
        setViewportView(textPanel);
        setRowHeaderView(new TextLineNumber(textPanel));
        textPanel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        setEditMode(true);
        loadScripts();
    }

    private void loadScripts() {
        try {
            exampleScript = Files.readString(Paths.get(scriptPath + "/example.txt").toAbsolutePath());
        } catch (IOException ex) {
            exampleScript = "Cannot load example script.";
        }
    }

    public void setScript(String fullScript) {
        textPanel.setText(fullScript);
    }

    public void setMode(EditorMode mode) {
        switch (mode) {
            case EDIT:
                setEditMode(true);
                setScript(userScript);
                break;
            case EXAMPLE:
                setEditMode(false);
                setScript(exampleScript);
                break;
        }
    }

    public void setEditMode(boolean nextEditMode) {
        if (editMode) {
            userScript = getScript();
        }
        textPanel.setEditable(nextEditMode);
        editMode = nextEditMode;
    }

    public String getScript() {
        return textPanel.getText();
    }
}
