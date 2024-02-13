package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class Toolbar extends JToolBar implements ActionListener {

    protected JButton createScriptButton(String name, String actionCommand) {
        JButton button = new JButton(name);

        button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        button.setActionCommand(actionCommand);
        button.addActionListener(this);

        return button;
    }

    protected Component createButtonSpacer() {
        return Box.createRigidArea(new Dimension(5, 1));
    }
}
