package MotherFocaTagTool;

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

/**
 * Created by Alvaro on 28/07/14.
 * Listador
 */

class Listador extends JFrame implements ActionListener{

    // Interfaz

    private JPanel panelLog;
    private JPanel panelLista;

    JTextArea textArea;
    JScrollPane messageArea;

    private JButton indexar, añadir, quitar;

    DefaultListModel listaDirectorios = new DefaultListModel();

    JList lista;

    // Traemos la clase ListaPeliculas

    ListaPeliculas listaPeliculas = new ListaPeliculas();

    // Json

    JSONObject jsonPeliculas = new JSONObject();

    // Config

    JSONObject jsonConfig;
    JSONArray jsonStoredDirs;

    String home = System.getProperty("user.home");
    String configFileDir = home + File.separator + ".configMFTT.json";
    File configFile = new File(configFileDir);

    //------------------------------------------------------------------------------
    //  Interefaz grafica
    //------------------------------------------------------------------------------

    public Listador() throws IOException {

        //------------------------------------------------------------------------------
        //  Crear la ventana
        //------------------------------------------------------------------------------

        setTitle("Crear listado");
        setSize(800, 500);
        Container contenedor = getContentPane();

        //------------------------------------------------------------------------------
        //  Crear los paneles de la ventana
        //------------------------------------------------------------------------------

        panelLog = new JPanel();
        panelLista = new JPanel();

        //------------------------------------------------------------------------------
        //  Añadir los paneles al contenedor
        //------------------------------------------------------------------------------

        contenedor.add(panelLog, "West");
        contenedor.add(panelLista);

        //------------------------------------------------------------------------------
        //  Panel del Log
        //------------------------------------------------------------------------------

        textArea = new JTextArea(29, 40);
        textArea.setEditable(false);
        messageArea = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelLog.add(messageArea);

        //------------------------------------------------------------------------------
        //  Panel de Acciones
        //------------------------------------------------------------------------------

        // Lista

        lista = new JList(listaDirectorios);
        JScrollPane scrollpane = new JScrollPane(lista);

        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panelLista.add(scrollpane, BorderLayout.EAST);

        // Añadir

        añadir = new JButton("Añadir");
        añadir.addActionListener(this);
        panelLista.add(añadir, BorderLayout.SOUTH);

        // Quitar

        quitar = new JButton("Quitar");
        quitar.addActionListener(this);
        panelLista.add(quitar, BorderLayout.SOUTH);

        // Indice

        indexar = new JButton("Indexar");
        indexar.addActionListener(this);
        panelLista.add(indexar, BorderLayout.SOUTH);


        //------------------------------------------------------------------------------
        //  Comprobacion de configuracion previa
        //------------------------------------------------------------------------------

        if(configFile.exists() && !configFile.isDirectory()) {

            // Existe

            jsonConfig = new JSONObject(new String(Files.readAllBytes(Paths.get(configFileDir))));

            if (jsonConfig.has("Directorios")){

                // Obtenemos el JsonArray del JsonObject

                jsonStoredDirs = jsonConfig.getJSONArray("Directorios");

                for (int i = 0; i < jsonStoredDirs.length(); i++){

                    // Añadimos a la lista uno a uno los elementos del array de directorios

                    listaDirectorios.addElement(jsonStoredDirs.getString(i));
                }

                lista.setModel( listaDirectorios );
                textArea.append("[done] Se ha cargado la configuracion\n");
            }
            else{
                textArea.append("[error] Fichero de configuracion existe, pero no contiene directorios\n");
            }
        }
        else{

            // No existe

            textArea.append("[info] No existe fichero de configuracion");

            jsonConfig = new JSONObject();
            jsonStoredDirs = new JSONArray();

        }

        jsonConfig.put("Directorios", jsonStoredDirs);
    }

    //------------------------------------------------------------------------------
    //  Escuchador
    //------------------------------------------------------------------------------

    public void actionPerformed(ActionEvent evento) {

        //--------------------------------------------------------------------------------
        //  Escuhador de indexar
        //--------------------------------------------------------------------------------

        if ( evento.getSource() == indexar ) {

            // Se ha pulsado el boton de indexar
            if (listaDirectorios.size() == 0) {
                textArea.append("No existen directorios a indexar\n");
            }
            else{

                for (int i = 0; i < listaDirectorios.size(); i++) {

                    // Obtenemos el i directorio de la lista

                    String directorio = (String) listaDirectorios.get(i) + File.separator;
                    textArea.append("Indexando (" + ((i + 1)) + File.separator + listaDirectorios.size() + "): " + directorio);
                    try {

                        // Para cada directorio de la lista obtenemos la lista de peliclas en foma de json

                        jsonPeliculas = listaPeliculas.creaIndice(textArea, directorio, jsonPeliculas);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    textArea.append(" (Done)\n");
                }

                // Escribimos el json en el fichero json de peliclas

                PrintWriter writer = null;
                try {
                    writer = new PrintWriter( System.getProperty("user.home") + File.separator + "peliculas.json", "UTF-8");
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

            int option = chooser.showOpenDialog(Listador.this);

            if (option == JFileChooser.APPROVE_OPTION) {

                // Si pulsamos seleccionar

                File[] folderList = chooser.getSelectedFiles();

                for (int i = 0; i < folderList.length; i++) {

                    listaDirectorios.addElement(folderList[i].getAbsolutePath());
                    textArea.append("Agregado directorio: " + folderList[i].getAbsolutePath() + "/\n");
                }

                lista.setModel( listaDirectorios );

                // Escribimos el fichero de configuracion

                jsonStoredDirs = new JSONArray(Arrays.asList(listaDirectorios.toArray()));

                jsonConfig.put("Directorios", jsonStoredDirs);

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

            jsonConfig.put("Directorios", jsonStoredDirs);

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

            textArea.append(" ** Eliminado **\n");
        }
    }

    //------------------------------------------------------------------------------
    //  Main
    //------------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {
        Listador ventana = new Listador() ;
        ventana.setVisible(true) ;
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}