package ui;

import ui.lib.TextLineNumber;

import javax.swing.*;
import java.awt.*;

public class ScriptEditorComponent extends JScrollPane {
    private JTextPane textPanel = new JTextPane();

    public ScriptEditorComponent() {
        setViewportView(textPanel);
        setRowHeaderView(new TextLineNumber(textPanel));

        textPanel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
    }

    public void setScript(String fullScript) {
        textPanel.setText(fullScript);
    }

    public String getScript() {
        return textPanel.getText();
    }
}
