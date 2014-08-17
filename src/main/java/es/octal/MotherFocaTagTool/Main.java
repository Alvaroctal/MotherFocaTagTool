package main.java.es.octal.MotherFocaTagTool;

import main.java.es.octal.MotherFocaTagTool.GUI.MainGUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Alvaro on 28/07/14.
 * MotherFocaTagTool
 */

class Main {

    //------------------------------------------------------------------------------
    //  Main
    //------------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        MainGUI mainWindow = new MainGUI();
        mainWindow.setVisible(true);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width  - mainWindow.getSize().width) / 2 - 100,
                (Toolkit.getDefaultToolkit().getScreenSize().height - mainWindow.getSize().height) / 2);
    }
}
