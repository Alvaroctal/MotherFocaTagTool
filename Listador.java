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

    String home = System.getProperty("user.home");
    public String configFileDir = home + File.separator + ".configMFTT.json";

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

	    JPanel toolbar = new JPanel();
	    toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
	    addButtons(toolbar);

        panelLog = new JPanel();
        tabbedPane = new JTabbedPane();

        panelPeliculas = new JPanel();
        panelSeries = new JPanel();

	    topPanel.add(toolbar, BorderLayout.NORTH);
        topPanel.add(panelLog, BorderLayout.WEST);
        topPanel.add(tabbedPane);

        //------------------------------------------------------------------------------
        //  Log
        //------------------------------------------------------------------------------

        log = new JTextArea(29, 40);
        log.setEditable(false);
        messageArea = new JScrollPane(log,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
        Listador ventana = new Listador() ;
        ventana.setVisible(true) ;
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

	//------------------------------------------------------------------------------
	//  Creación de botones para el panel superior
	//------------------------------------------------------------------------------
	public void addButtons(JPanel toolbar) {
		JButton btn;
		JLabel lab = new JLabel("  ");
		int size = 64; // Button size in pixels (squared)

		// Index button
		btn = new JButton("Indexar", new ImageIcon(getClass().getResource("resources/icons/list-32.png")));
		btn.setPreferredSize(new Dimension(size, size));
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn.setHorizontalTextPosition(SwingConstants.CENTER);
		toolbar.add(btn, BorderLayout.WEST);

		// Spacer
		toolbar.add(lab);

		// Tag button
		btn = new JButton("Tagear", new ImageIcon(getClass().getResource("resources/icons/tag-32.png")));
		btn.setPreferredSize(new Dimension(size, size));
		btn.setMargin(new Insets(0,0,0,0));
		btn.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn.setHorizontalTextPosition(SwingConstants.CENTER);
		toolbar.add(btn, BorderLayout.WEST);
	}
}
