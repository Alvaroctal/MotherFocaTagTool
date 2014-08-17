package main.java.es.octal.MotherFocaTagTool.GUI;

import main.java.es.octal.MotherFocaTagTool.GUI.config.FTPConfigGUI;
import main.java.es.octal.MotherFocaTagTool.GUI.media.PeliculasGUI;
import main.java.es.octal.MotherFocaTagTool.GUI.media.SeriesGUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Alvaro on 17/08/14.
 * MotherFocaTagTool
 */

public class MainGUI extends JFrame {

    // Interfaz

    private JPanel topPanel;

    private JPanel panelIzquierdo, panelDerecho;
    private JTabbedPane tabbedMedia, tabbedConfig;

    JTextArea log;
    JScrollPane messageArea;

    // Config

    public String configFileDir = System.getProperty("user.home") + File.separator + ".configMFTT.json";

    // Constructor

    public MainGUI() throws IOException {

        //------------------------------------------------------------------------------
        //  Crear la ventana
        //------------------------------------------------------------------------------

        setTitle("Crear listado");
        setSize(800, 500);
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        getContentPane().add(topPanel);

        //------------------------------------------------------------------------------
        //  Crear Paneles
        //------------------------------------------------------------------------------

        panelIzquierdo = new JPanel();
        panelDerecho = new JPanel();

        tabbedMedia = new JTabbedPane();
        tabbedConfig = new JTabbedPane();

        // Alineaciones

        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));

        panelDerecho.add(tabbedMedia);
        panelDerecho.add(tabbedConfig);

        topPanel.add(panelIzquierdo);
        topPanel.add(panelDerecho);

        //------------------------------------------------------------------------------
        //  Log
        //------------------------------------------------------------------------------

        log = new JTextArea(29, 38);
        log.setEditable(false);
        messageArea = new JScrollPane(log,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panelIzquierdo.add(messageArea);

        //------------------------------------------------------------------------------
        //  Crear las pestañas
        //------------------------------------------------------------------------------

        tabbedMedia.addTab("Peliculas", new PeliculasGUI(log, configFileDir));
        tabbedMedia.addTab("Series", new SeriesGUI(log, configFileDir));

        tabbedConfig.addTab("FTP", new FTPConfigGUI(log, configFileDir));

    }
}
