package MotherFocaTagTool.GUI;

import MotherFocaTagTool.list.ListaPeliculas;
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

public class PeliculasGUI extends Component implements ActionListener {

    // log

    JTextArea log;

    // Botones

    private JButton añadir;
    private JButton quitar;
    private JButton indexar;

    // Lista

    JList lista;
    JScrollPane scrollLista;
    DefaultListModel listaDirectorios = new DefaultListModel();

    // Json

    JSONObject jsonConfig;
    JSONObject jsonPeliculasConfig;
    JSONArray jsonStoredDirs;

    JSONObject jsonPeliculas;

    // File

    File configFile;
    public String configFileDir;

    // Clase

    ListaPeliculas listaPeliculas = new ListaPeliculas();

    // constructor

    public PeliculasGUI(JTextArea log, JPanel panel, String configFileDir) throws IOException {

        // Config

        this.configFileDir = configFileDir;
        configFile = new File(configFileDir);

        // Log

        this.log = log;

        // Lista

        lista = new JList(listaDirectorios);
        scrollLista = new JScrollPane(lista);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(scrollLista);

        // Añadir

        añadir = new JButton("Añadir");
        añadir.addActionListener(this);
        panel.add(añadir, BorderLayout.SOUTH);

        // Quitar

        quitar = new JButton("Quitar");
        quitar.addActionListener(this);
        panel.add(quitar, BorderLayout.SOUTH);

        // Indice

        indexar = new JButton("Indexar");
        indexar.addActionListener(this);
        panel.add(indexar, BorderLayout.SOUTH);

        //------------------------------------------------------------------------------
        //  Comprobacion de configuracion previa
        //------------------------------------------------------------------------------

        if(configFile.exists() && !configFile.isDirectory()) {

            // El fichero de configuracion existe

            jsonConfig = new JSONObject(new String(Files.readAllBytes(Paths.get(configFileDir))));

            if (jsonConfig.has("Peliculas")) {

                // El fichero de configuracion contiene configuracion sobre peliculas

                jsonPeliculasConfig = jsonConfig.getJSONObject("Peliculas");

                if (jsonPeliculasConfig.has("Directorios")) {

                    // La configuracion sobre peliculas contiene directorios

                    jsonStoredDirs = jsonPeliculasConfig.getJSONArray("Directorios");

                    for (int i = 0; i < jsonStoredDirs.length(); i++) {

                        // Añadimos a la lista uno a uno los elementos del array de directorios

                        listaDirectorios.addElement(jsonStoredDirs.getString(i));
                    }

                    lista.setModel(listaDirectorios);
                    log.append("[peliculas] Se ha cargado la configuracion\n");
                }
                else {

                    // Fichero de configuracion contiene configuracion de peliculas, pero no directorios (raro)

                    log.append("[error] (Peliculas) Fichero de configuracion existe, pero no contiene directorios\n");
                }
            }
            else {

                // Fichero de configuracion no contiene configuraciones de peliculas

                log.append("[error] (Peliculas) Fichero de configuracion existe, pero no contiene configuraciones\n");
            }
        }
        else {

            // Fichero de configuracion no existe

            log.append("[info] (Peliculas) No existe fichero de configuracion");

            jsonConfig = new JSONObject();
            jsonPeliculasConfig = new JSONObject();
            jsonStoredDirs = new JSONArray();

        }

        jsonPeliculasConfig.put("Directorios", jsonStoredDirs);
        jsonConfig.put("Peliculas", jsonPeliculasConfig);
    }

    //------------------------------------------------------------------------------
    //  Escuchadores
    //------------------------------------------------------------------------------

    public void actionPerformed(ActionEvent evento) {

        //------------------------------------------------------------------------------
        //  Escuhador de indexar
        //------------------------------------------------------------------------------

        if (evento.getSource() == indexar) {

            // Se ha pulsado el boton de indexar

            if (listaDirectorios.size() == 0) {
                log.append("No existen directorios a indexar\n");
            } else {

                // Creamos un json de peliculas

                jsonPeliculas = new JSONObject();

                for (int i = 0; i < listaDirectorios.size(); i++) {

                    // Obtenemos el i directorio de la lista

                    String directorio = (String) listaDirectorios.get(i) + File.separator;
                    log.append("Indexando (" + ((i + 1)) + File.separator + listaDirectorios.size() + "): " + directorio);
                    try {

                        // Para cada directorio de la lista obtenemos la lista de peliclas en foma de json

                        jsonPeliculas = listaPeliculas.creaIndice(log, directorio, jsonPeliculas);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    log.append(" (Done)\n");
                }

                // Escribimos el json en el fichero json de peliclas

                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(System.getProperty("user.home") + File.separator + "peliculas.json", "UTF-8");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                writer.println(jsonPeliculas.toString());
                writer.close();
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

                lista.setModel( listaDirectorios );

                // Escribimos el fichero de configuracion

                jsonStoredDirs = new JSONArray(Arrays.asList(listaDirectorios.toArray()));

                jsonPeliculasConfig.put("Directorios", jsonStoredDirs);
                jsonConfig.put("Peliculas", jsonPeliculasConfig);

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

            listaDirectorios.removeElementAt(lista.getSelectedIndex());
            lista.setModel( listaDirectorios );

            // Escribir el json de configuracion

            jsonStoredDirs = new JSONArray(Arrays.asList(listaDirectorios.toArray()));

            jsonPeliculasConfig.put("Directorios", jsonStoredDirs);
            jsonConfig.put("Peliculas", jsonPeliculasConfig);

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
    }
}

