package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class BottomToolbar extends Toolbar {
    private static final String PREVIEW_ACTION = "preview";
    private static final String ORGANIZE_ACTION = "organize";
    private static final String INFO_ACTION = "info";

    private final MainPanel mainPanel;

    public BottomToolbar(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        add(createScriptButton("Reference sheet", INFO_ACTION));
        // take up left side of space
        add(Box.createHorizontalGlue());

        // add buttons on the right

        add(createScriptButton("Preview", PREVIEW_ACTION));
        add(createButtonSpacer());
        add(createScriptButton("Organize", ORGANIZE_ACTION));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(PREVIEW_ACTION)) {
            mainPanel.previewScript();
        } else if (e.getActionCommand().equals(ORGANIZE_ACTION)){
            mainPanel.executeScript();
        } else if (e.getActionCommand().equals(INFO_ACTION)){
            mainPanel.showDocs();
        }
    }
}
