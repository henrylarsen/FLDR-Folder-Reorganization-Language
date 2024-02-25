package ui;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("FLDR: FLDR Language for Directory Reorganization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new MainPanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
