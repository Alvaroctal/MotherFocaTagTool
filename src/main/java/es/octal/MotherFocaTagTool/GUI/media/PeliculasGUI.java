package main.java.es.octal.MotherFocaTagTool.GUI.media;

import main.java.es.octal.MotherFocaTagTool.GUI.config.data.Config;
import main.java.es.octal.MotherFocaTagTool.list.ListaPeliculas;
import main.java.org.apache.commons.net.net.ftp.FTPClient;
import main.java.org.json.JSONArray;
import main.java.org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;

/**
 * Created by Alvaro on 17/08/14.
 * MotherFocaTagTool
 */

public class PeliculasGUI extends JPanel implements ActionListener {

    // Externas

    private Config config;
    private JTextArea log;

    // Paneles

    private JPanel panelLista;
    private JPanel panelListaBotones;
    private JPanel panelCheckBoxes;

    // Lista

    private JList lista;
    private JScrollPane scrollLista;
    private DefaultListModel listaDirectorios;

    // Botones

    private JButton añadir, quitar, indexar;

    // CheckBoxes

    private JCheckBox showOnlyFails;
    private JCheckBox ftpUpload;

    // Clase

    private ListaPeliculas listaPeliculas = new ListaPeliculas();

    // JSON

    private JSONObject jsonPeliculas;

    // constructor

    public PeliculasGUI(JTextArea log, Config config) throws IOException {

        //------------------------------------------------------------------------------
        //  Variables externas
        //------------------------------------------------------------------------------

        this.config = config;
        this.log = log;

        //------------------------------------------------------------------------------
        //  Configuracion
        //------------------------------------------------------------------------------

        listaDirectorios = config.peliculas.getDirsList();

        //------------------------------------------------------------------------------
        //  Paneles
        //------------------------------------------------------------------------------

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        panelLista = new JPanel();
        panelListaBotones = new JPanel();
        panelCheckBoxes = new JPanel();

        this.add(panelLista);
        this.add(panelCheckBoxes);

        // Alineaciones

        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));

        panelListaBotones.setLayout(new BoxLayout(panelListaBotones, BoxLayout.X_AXIS));

        panelCheckBoxes.setLayout(new BoxLayout(panelCheckBoxes, BoxLayout.Y_AXIS));
        panelCheckBoxes.setAlignmentX(JCheckBox.RIGHT_ALIGNMENT);

        //------------------------------------------------------------------------------
        //  Lista
        //------------------------------------------------------------------------------

        lista = new JList(listaDirectorios);
        scrollLista = new JScrollPane(lista);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollLista.setPreferredSize(new Dimension(265, 20));
        panelLista.add(scrollLista);
        panelLista.add(panelListaBotones);

        //------------------------------------------------------------------------------
        //  ListaBotones
        //------------------------------------------------------------------------------

        // Añadir

        añadir = new JButton("Añadir");
        añadir.addActionListener(this);
        panelListaBotones.add(añadir);

        // Quitar

        quitar = new JButton("Quitar");
        quitar.addActionListener(this);
        panelListaBotones.add(quitar);

        // Indexar

        indexar = new JButton("Indexar");
        indexar.addActionListener(this);
        panelListaBotones.add(indexar);

        if ( System.getProperty( "os.name" ).toLowerCase( ).startsWith( "mac os x" ) ){

            // Es mac

            añadir.putClientProperty( "JButton.buttonType", "segmented" );
            añadir.putClientProperty( "JButton.segmentPosition", "first" );

            quitar.putClientProperty( "JButton.buttonType", "segmented" );
            quitar.putClientProperty("JButton.segmentPosition", "last");

            indexar.putClientProperty( "JButton.buttonType", "segmented" );
            indexar.putClientProperty( "JButton.segmentPosition", "only" );
        }

        //------------------------------------------------------------------------------
        //  CheckBoxes
        //------------------------------------------------------------------------------

        // ShowOnlyFails

        showOnlyFails = new JCheckBox("Notificar solo fallos");
        showOnlyFails.setSelected(false);
        panelCheckBoxes.add(showOnlyFails);

        // FTP

        ftpUpload = new JCheckBox("Subir al FTP");
        ftpUpload.setSelected(true);
        panelCheckBoxes.add(ftpUpload);

    }

    //------------------------------------------------------------------------------
    //  Escuchadores
    //------------------------------------------------------------------------------

    public void actionPerformed(ActionEvent evento) {

        //------------------------------------------------------------------------------
        //  Escuchador de indexar
        //------------------------------------------------------------------------------

        if (evento.getSource() == indexar) {

            // Se ha pulsado el boton de indexar, limpiamos el log

            log.setText(null);

            listaDirectorios = config.peliculas.getDirsList();

            if (listaDirectorios.size() == 0) {
                log.append("No existen directorios a indexar\n");
            }
            else {

                // Creamos un json de peliculas

                jsonPeliculas = new JSONObject();

                Boolean noFailGlobal = true;

                for (int i = 0; i < listaDirectorios.size(); i++) {

                    // Obtenemos el i directorio de la lista

                    String directorio = (String) listaDirectorios.get(i);
                    log.append("Indexando (" + ((i + 1)) + "/" + listaDirectorios.size() + "): " + directorio + "\n");
                    try {

                        // Para cada directorio de la lista obtenemos la lista de peliculas en forma de json

                        jsonPeliculas = listaPeliculas.creaIndice(log, directorio, jsonPeliculas, showOnlyFails.isSelected());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (! jsonPeliculas.getBoolean("noFail")) {
                        noFailGlobal = false;
                    }
                }
                if (! noFailGlobal){
                    log.append("*************************************************************\n");
                    log.append("[warn] Se detectaron 1 o mas errores de sintaxis\n");
                }

                //------------------------------------------------------------------------------
                //  Fichero local
                //------------------------------------------------------------------------------

                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(config.peliculas.getfilePath(), "UTF-8");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                writer.println(jsonPeliculas.toString(4));
                writer.close();

                //------------------------------------------------------------------------------
                //  FTP
                //------------------------------------------------------------------------------

                if (ftpUpload.isSelected()) {
                    if (jsonPeliculas.getBoolean("noFail")) {

                        // El fichero de configuracion contiene configuracion sobre peliculas

                        FTPClient ftpClient = new FTPClient();

                        //------------------------------------------------------------------------------
                        //  FTP conect
                        //------------------------------------------------------------------------------

                        try {
                            ftpClient.connect(config.ftp.getServer());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (ftpClient.login(config.ftp.getUser(), config.ftp.getPass())) {
                                log.append("[ftp] acceso concedido\n");
                            } else {
                                log.append("[ftp] acceso denegado\n");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //------------------------------------------------------------------------------
                        //  FTP upload
                        //------------------------------------------------------------------------------

                        try {

                            InputStream inputStream = new FileInputStream(new File(config.peliculas.getfilePath()));

                            log.append("[ftp] Iniciando subida al servidor...\n");
                            boolean done = ftpClient.storeFile(config.peliculas.getFtpFilePath(), inputStream);
                            inputStream.close();
                            if (done) {
                                log.append("[ftp] Subida completada\n");
                            } else {
                                log.append("[ftp] Error, no se pudo subir el archivo\n");
                            }
                        } catch (IOException ioe) {
                            log.append("[error] No se pudo subir el archivo al servidor\n");
                        }
                    } else {
                        log.append("[ftp] Se encontraron errores, subida cancelada\n");
                    }
                }
                else {
                    // ftp deshabilitado
                }
            }
        }

        //------------------------------------------------------------------------------
        //  Escuchador de Añadir
        //------------------------------------------------------------------------------

        else if ( evento.getSource() == añadir ) {

            // Configuramos el selector de directorios

            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int option = chooser.showOpenDialog(PeliculasGUI.this);

            if (option == JFileChooser.APPROVE_OPTION) {

                // Si pulsamos seleccionar

                File[] folderList = chooser.getSelectedFiles();

                for (int i = 0; i < folderList.length; i++) {

                    listaDirectorios.addElement(folderList[i].getAbsolutePath());
                    log.append("Agregado directorio: " + folderList[i].getAbsolutePath() + "/\n");
                }

                // Actualizar la lista

                lista.setModel( listaDirectorios );

                // Actializar la configuracion

                config.peliculas.edit(new JSONArray(Arrays.asList(listaDirectorios.toArray())));

                // Guardar la configuracion

                try {
                    config.save();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        //------------------------------------------------------------------------------
        //  Escuchador de Quitar
        //------------------------------------------------------------------------------

        else if ( evento.getSource() == quitar ) {

            // Se ha pulsado el boton de quitar

            try {
                listaDirectorios.removeElementAt(lista.getSelectedIndex());

                // Actualizamos la lista

                lista.setModel(listaDirectorios);

                // Actualizamos la configuracion

                config.peliculas.edit(new JSONArray(Arrays.asList(listaDirectorios.toArray())));

                // Guardamos la configuracion

                config.save();

                log.append("[peliculas] Directorio eliminado\n");
            }
            catch (ArrayIndexOutOfBoundsException e){
                log.append("No has seleccionado ningun directorio\n");
                System.out.print(lista.getSelectedIndex());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        //------------------------------------------------------------------------------
        //  Escuchador de else
        //------------------------------------------------------------------------------

        else{
            log.append("[warn] No existe accion asociada al boton\n");
        }
    }
}

