package ui;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InfoDialog extends JDialog {
    String infoMessage;
    public InfoDialog(String title) {
        setTitle(title);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setSize(400,600);
        JEditorPane editorPane = new JEditorPane();
        loadMessage();
        editorPane.setContentType("text/html");
        editorPane.setText(infoMessage);
        editorPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(editorPane);
        add(scrollPane);
    }

    private void loadMessage() {
        try {
            infoMessage = Files.readString(Paths.get("./src/ui/documentation/info.html").toAbsolutePath());
        } catch (IOException ex) {
            infoMessage = "Documentation currently unavailable.";
        }
    }
}
