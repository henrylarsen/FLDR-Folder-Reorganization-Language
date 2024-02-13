package ui;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("File Organization Language"); // TODO
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new MainPanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
