package MotherFocaTagTool.GUI;

import MotherFocaTagTool.list.ListaSeries;
import MotherFocaTagTool.org.apache.commons.net.net.ftp.FTPClient;
import MotherFocaTagTool.org.json.JSONArray;
import MotherFocaTagTool.org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class SeriesGUI extends Component implements ActionListener {

    // Paneles

    private JPanel panelLista;
    private JPanel panelListaBotones;
    private JPanel panelCheckBoxes;
    private JPanel panelBotones;

    // log

    JTextArea log;

    // Lista

    JList lista;
    JScrollPane scrollLista;
    DefaultListModel listaDirectorios = new DefaultListModel();

    // Botones

    private JButton añadir, quitar, configurar, tagear, indexar;
    int buttonSize = 64;

    // CheckBoxes

    private JCheckBox showOnlyFails;
    private JCheckBox ftpUpload;

    // Json

    JSONObject jsonConfig;
    JSONObject jsonSeriesConfig;
    JSONArray jsonStoredDirs;

    JSONObject jsonSeries;

    // File

    File configFile;
    public String configFileDir;
    public String jsonSeriesDir = System.getProperty("user.home") + File.separator + "series.json";

    // Clase

    ListaSeries listaSeries = new ListaSeries();

    // constructor

    public SeriesGUI(JTextArea log, JPanel panel, String configFileDir) throws IOException {

        // Config

        this.configFileDir = configFileDir;
        configFile = new File(configFileDir);

        // Log

        this.log = log;

        //------------------------------------------------------------------------------
        //  Paneles
        //------------------------------------------------------------------------------

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panelLista = new JPanel();
        panelListaBotones = new JPanel();
        panelCheckBoxes = new JPanel();
        panelBotones = new JPanel();

        panel.add(panelLista);
        panel.add(panelListaBotones);
        panel.add(panelCheckBoxes);
        panel.add(panelBotones);

        // Alineaciones

        panelCheckBoxes.setLayout(new BoxLayout(panelCheckBoxes, BoxLayout.Y_AXIS));
        panelCheckBoxes.setAlignmentX(JCheckBox.RIGHT_ALIGNMENT);

        //------------------------------------------------------------------------------
        //  Lista
        //------------------------------------------------------------------------------

        lista = new JList(listaDirectorios);
        scrollLista = new JScrollPane(lista);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollLista.setPreferredSize(new Dimension(265, 200));
        panelLista.add(scrollLista);

        //------------------------------------------------------------------------------
        //  ListaBotones
        //------------------------------------------------------------------------------

        // Añadir

        añadir = new JButton("Añadir");
        añadir.addActionListener(this);
        panelListaBotones.add(añadir, BorderLayout.SOUTH);

        // Quitar

        quitar = new JButton("Quitar");
        quitar.addActionListener(this);
        panelListaBotones.add(quitar, BorderLayout.SOUTH);

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

        //------------------------------------------------------------------------------
        //  Botones
        //------------------------------------------------------------------------------

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));

        // Configurar

        configurar = new JButton("  Ajustes  ", new ImageIcon(getClass().getResource("resources/icons/settings-32.png")));
        configurar.setPreferredSize(new Dimension(buttonSize, buttonSize));
        configurar.setMargin(new Insets(0,0,0,0));
        configurar.setVerticalTextPosition(SwingConstants.BOTTOM);
        configurar.setHorizontalTextPosition(SwingConstants.CENTER);
        configurar.addActionListener(this);
        toolbar.add(configurar, BorderLayout.SOUTH);
        toolbar.add(new JLabel("  "));

        // Tagear

        tagear = new JButton("  Tagear  ", new ImageIcon(getClass().getResource("resources/icons/tag-32.png")));
        tagear.setPreferredSize(new Dimension(buttonSize + 20, buttonSize));
        tagear.setMargin(new Insets(0,0,0,0));
        tagear.setVerticalTextPosition(SwingConstants.BOTTOM);
        tagear.setHorizontalTextPosition(SwingConstants.CENTER);
        tagear.addActionListener(this);
        toolbar.add(tagear, BorderLayout.SOUTH);
        toolbar.add(new JLabel("  "));

        // Indexar

        indexar = new JButton("  Indexar  ", new ImageIcon(getClass().getResource("resources/icons/list-32.png")));
        indexar.setPreferredSize(new Dimension(buttonSize, buttonSize));
        indexar.setMargin(new Insets(0, 0, 0, 0));
        indexar.setVerticalTextPosition(SwingConstants.BOTTOM);
        indexar.setHorizontalTextPosition(SwingConstants.CENTER);
        indexar.addActionListener(this);
        toolbar.add(indexar, BorderLayout.SOUTH);

        panelBotones.add(toolbar, "South");

        //------------------------------------------------------------------------------
        //  Comprobacion de configuracion previa
        //------------------------------------------------------------------------------

        if(configFile.exists() && !configFile.isDirectory()) {

            // El fichero de configuracion existe

            jsonConfig = new JSONObject(new String(Files.readAllBytes(Paths.get(configFileDir))));

            if (jsonConfig.has("Series")) {

                // El fichero de configuracion contiene configuracion sobre series

                jsonSeriesConfig = jsonConfig.getJSONObject("Series");

                if (jsonSeriesConfig.has("Directorios")) {

                    // La configuracion sobre series contiene directorios

                    jsonStoredDirs = jsonSeriesConfig.getJSONArray("Directorios");

                    for (int i = 0; i < jsonStoredDirs.length(); i++) {

                        // Añadimos a la lista uno a uno los elementos del array de directorios

                        listaDirectorios.addElement(jsonStoredDirs.getString(i));
                    }

                    lista.setModel(listaDirectorios);
                    log.append("[series] Se ha cargado la configuracion\n");
                }
                else {

                    // Fichero de configuracion contiene configuracion de series, pero no directorios (raro)

                    log.append("[error] (Series) Fichero de configuracion existe, pero no contiene directorios\n");
                }
            }
            else {

                // Fichero de configuracion no contiene configuraciones de series

                log.append("[error] (Series) Fichero de configuracion existe, pero no contiene configuraciones\n");
            }
        }
        else {

            // Fichero de configuracion no existe

            log.append("[info] (Series) No existe fichero de configuracion\n");

            jsonConfig = new JSONObject();
            jsonSeriesConfig = new JSONObject();
            jsonStoredDirs = new JSONArray();

        }

        jsonSeriesConfig.put("Directorios", jsonStoredDirs);
        jsonConfig.put("Series", jsonSeriesConfig);
    }

    //------------------------------------------------------------------------------
    //  Escuchadores
    //------------------------------------------------------------------------------

    public void actionPerformed(ActionEvent evento) {

        //------------------------------------------------------------------------------
        //  Escuhador de indexar
        //------------------------------------------------------------------------------

        if (evento.getSource() == indexar) {

            // Se ha pulsado el boton de indexar, limpiamos el log

            log.setText(null);

            if (listaDirectorios.size() == 0) {
                log.append("No existen directorios a indexar\n");
            } else {

                // Creamos un json de series

                jsonSeries = new JSONObject();

                boolean noFailGlobal = true;

                for (int i = 0; i < listaDirectorios.size(); i++) {

                    // Obtenemos el i directorio de la lista

                    String directorio = (String) listaDirectorios.get(i);
                    log.append("Indexando (" + ((i + 1)) + File.separator + listaDirectorios.size() + "): " + directorio + "\n");
                    try {

                        // Para cada directorio de la lista obtenemos la lista de series en forma de json

                        jsonSeries = listaSeries.creaIndice(log, directorio, jsonSeries, showOnlyFails.isSelected());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (! jsonSeries.getBoolean("noFail")) {
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
                    writer = new PrintWriter(jsonSeriesDir, "UTF-8");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                writer.println(jsonSeries.toString());
                writer.close();

                //------------------------------------------------------------------------------
                //  FTP
                //------------------------------------------------------------------------------

                if (ftpUpload.isSelected()) {
                    if (jsonConfig.has("ftp")) {
                        if (jsonSeries.getBoolean("noFail")) {

                            // El fichero de configuracion contiene configuracion sobre series

                            JSONObject jsonFtpConfig = jsonConfig.getJSONObject("ftp");

                            FTPClient ftpClient = new FTPClient();

                            String sFTP = jsonFtpConfig.getString("server");
                            String sUser = jsonFtpConfig.getString("user");
                            String sPassword = jsonFtpConfig.getString("pass");

                            //------------------------------------------------------------------------------
                            //  FTP conect
                            //------------------------------------------------------------------------------

                            try {
                                ftpClient.connect(sFTP);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                if (ftpClient.login(sUser,sPassword)){
                                    log.append("[ftp] acceso concedido\n");
                                }
                                else{
                                    log.append("[ftp] acceso denegado\n");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //------------------------------------------------------------------------------
                            //  FTP upload
                            //------------------------------------------------------------------------------

                            try {
                                File firstLocalFile = new File(jsonSeriesDir);

                                String firstRemoteFile = jsonSeriesConfig.getString("ftp");
                                InputStream inputStream = new FileInputStream(firstLocalFile);

                                log.append("[ftp] Iniciando subida al servidor...\n");
                                boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
                                inputStream.close();
                                if (done) {
                                    log.append("[ftp] Subida completada\n");
                                } else {
                                    log.append("[ftp] Error, no se pudo subir el archivo\n");
                                }
                            } catch (IOException ioe) {
                                log.append("[error] No se pudo subir el archivo al servidor");
                            }
                        } else {
                            log.append("[ftp] Se encontraron errores, subida cancelada\n");
                        }
                    } else {
                        log.append("[ftp] json data missing\n");
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

            int option = chooser.showOpenDialog(SeriesGUI.this);

            if (option == JFileChooser.APPROVE_OPTION) {

                // Si pulsamos seleccionar

                File[] folderList = chooser.getSelectedFiles();

                for (int i = 0; i < folderList.length; i++) {

                    listaDirectorios.addElement(folderList[i].getAbsolutePath());
                    log.append("Agregado directorio: " + folderList[i].getAbsolutePath() + "/\n");
                }

                lista.setModel( listaDirectorios );

                // Escribimos el fichero de configuracion

                jsonStoredDirs = new JSONArray(Arrays.asList(listaDirectorios.toArray()));

                jsonSeriesConfig.put("Directorios", jsonStoredDirs);
                jsonConfig.put("Series", jsonSeriesConfig);

                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(configFileDir, "UTF-8");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                writer.println(jsonConfig.toString());
                writer.close();
            }
        }

        //------------------------------------------------------------------------------
        //  Escuchador de Quitar
        //------------------------------------------------------------------------------

        else if ( evento.getSource() == quitar ) {

            // Se ha pulsado el boton de quitar

            try {
                listaDirectorios.removeElementAt(lista.getSelectedIndex());

                lista.setModel(listaDirectorios);

                // Escribir el json de configuracion

                jsonStoredDirs = new JSONArray(Arrays.asList(listaDirectorios.toArray()));

                jsonSeriesConfig.put("Directorios", jsonStoredDirs);
                jsonConfig.put("Series", jsonSeriesConfig);

                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(configFileDir, "UTF-8");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                writer.println(jsonConfig.toString());
                writer.close();

                log.append(" ** Eliminado **\n");
            }
            catch (ArrayIndexOutOfBoundsException e){
                log.append("No has seleccionado ningun directorio\n");
            }
        }

        //------------------------------------------------------------------------------
        //  Escuchador de Configurar
        //------------------------------------------------------------------------------

        else if ( evento.getSource() == configurar ) {
            log.append("WIP - Octal\n");
        }
        else if ( evento.getSource() == tagear ) {
            log.append("WIP - Bio\n");
        }
        else{
            log.append("[warn] No Existe accion asociada al boton\n");
        }
    }
}
