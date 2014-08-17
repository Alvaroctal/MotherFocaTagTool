package main.java.es.octal.MotherFocaTagTool.GUI;

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

    private JPanel panelLog;
    private JTabbedPane tabbedPane;

    private JPanel panelPeliculas;
    private JPanel panelSeries;

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

        panelLog = new JPanel();
        tabbedPane = new JTabbedPane();

        panelPeliculas = new JPanel();
        panelSeries = new JPanel();

        topPanel.add(panelLog, BorderLayout.WEST);
        topPanel.add(tabbedPane);

        //------------------------------------------------------------------------------
        //  Log
        //------------------------------------------------------------------------------

        log = new JTextArea(29, 40);
        log.setEditable(false);
        messageArea = new JScrollPane(log,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panelLog.add(messageArea);

        //------------------------------------------------------------------------------
        //  Crear las pestañas
        //------------------------------------------------------------------------------


        PeliculasGUI peliculasGUI = new PeliculasGUI(log, panelPeliculas, configFileDir);
        SeriesGUI seriesGUI = new SeriesGUI(log, panelSeries, configFileDir);

        tabbedPane.addTab("Peliculas", panelPeliculas);
        tabbedPane.addTab("Series", panelSeries);

    }
}
