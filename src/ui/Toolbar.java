package ui;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class Toolbar extends JToolBar implements ActionListener {

    protected JButton createScriptButton(String name, String actionCommand) {
        JButton button = new JButton(name);

        button.setActionCommand(actionCommand);
        button.addActionListener(this);

        return button;
    }
}
