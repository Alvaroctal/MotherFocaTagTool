package MotherFocaTagTool;

import MotherFocaTagTool.GUI.PeliculasGUI;
import MotherFocaTagTool.GUI.SeriesGUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Alvaro on 28/07/14.
 * Listador
 */

class Listador extends JFrame {

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

    public Listador() throws IOException {

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

        crearPanelPeliculas();
        crearPanelSeries();

        tabbedPane.addTab("Peliculas", panelPeliculas);
        tabbedPane.addTab("Series", panelSeries);

    }

    //------------------------------------------------------------------------------
    //  Main
    //------------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Listador ventana = new Listador();
        ventana.setVisible(true);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width  - ventana.getSize().width) / 2 - 100,
                (Toolkit.getDefaultToolkit().getScreenSize().height - ventana.getSize().height) / 2);
    }

    //------------------------------------------------------------------------------
    //  Pesataña de Peliculas
    //------------------------------------------------------------------------------

    public void crearPanelPeliculas() throws IOException {

        panelPeliculas = new JPanel();

        PeliculasGUI peliculasGUI = new PeliculasGUI(log, panelPeliculas, configFileDir);
    }

    //------------------------------------------------------------------------------
    //  Pestaña de Series
    //------------------------------------------------------------------------------

    public void crearPanelSeries() throws IOException {

        panelSeries = new JPanel();

        SeriesGUI seriesGUI = new SeriesGUI(log, panelSeries, configFileDir);
    }
}
