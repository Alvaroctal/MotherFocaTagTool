package MotherFocaTagTool;

import MotherFocaTagTool.org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

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

    ListaPeliculas listador = new ListaPeliculas();

    // Json

    JSONObject jsonFile = new JSONObject();

    // Config

    JSONObject configFile = new JSONObject();

    //------------------------------------------------------------------------------
    //  Interefaz grafica
    //------------------------------------------------------------------------------

    public Listador() {

        //--------------------------------------------------------------------------------
        //  Crear la ventana
        //--------------------------------------------------------------------------------

        setTitle("Crear listado");
        setSize(800, 500);
        Container contenedor = getContentPane();

        //--------------------------------------------------------------------------------
        //  Crear los paneles de la ventana
        //--------------------------------------------------------------------------------

        panelLog = new JPanel();
        panelLista = new JPanel();

        //--------------------------------------------------------------------------------
        //  Añadir los paneles al contenedor
        //--------------------------------------------------------------------------------

        contenedor.add(panelLog, "West");
        contenedor.add(panelLista);

        //--------------------------------------------------------------------------------
        //  Panel del Log
        //--------------------------------------------------------------------------------

        textArea = new JTextArea(29, 40);
        textArea.setEditable(false);
        messageArea = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelLog.add(messageArea);

        //--------------------------------------------------------------------------------
        //  Panel de Acciones
        //--------------------------------------------------------------------------------

        // Lista

        lista = new JList(listaDirectorios);
        JScrollPane scrollpane = new JScrollPane(lista);

        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.addListSelectionListener(new ListSelectionListener() {

            // Escuhador de campo seleccionado

            public void valueChanged(ListSelectionEvent le) {
                int idx = lista.getSelectedIndex();
                if (idx != -1)
                    System.out.println("Current selection: " + listaDirectorios.get(idx));
                else
                    System.out.println("[error] Campo no valido");
            }
        });

        panelLista.add(scrollpane, BorderLayout.EAST);

        // Añadir

        añadir = new JButton("Añadir");
        añadir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

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
                        lista.setModel( listaDirectorios );
                        textArea.append("Agregado directorio: " + folderList[i].getAbsolutePath() + "/\n");
                    }
                }
            }
        });

        panelLista.add(añadir, BorderLayout.SOUTH);

        // Quitar

        quitar = new JButton("Quitar");
        quitar.addActionListener(this);
        panelLista.add(quitar, BorderLayout.SOUTH);

        // Indice

        indexar = new JButton("Indexar");
        indexar.addActionListener(this);
        panelLista.add(indexar, BorderLayout.SOUTH);

    }

    //------------------------------------------------------------------------------
    //  Escuchador
    //------------------------------------------------------------------------------

    public void actionPerformed(ActionEvent evento) {

        //--------------------------------------------------------------------------------
        //  Añadimos las acciones para los eventos del panel de numeros
        //--------------------------------------------------------------------------------

        if ( evento.getSource() == indexar ) {

            // Se ha pulsado el boton de indexar
            if (listaDirectorios.size() == 0) {
                textArea.append("No existen directorios a indexar\n");
            }
            else{

                for (int i = 0; i < listaDirectorios.size(); i++) {
                    String directorio = (String) listaDirectorios.get(i) + "/";
                    textArea.append("Indexando (" + ((i + 1)) + "/" + listaDirectorios.size() + "): " + directorio);
                    try {
                        jsonFile = listador.listaPeliculas(textArea, directorio, jsonFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    textArea.append(" (Done)\n");
                }

                PrintWriter writer = null;
                try {
                    writer = new PrintWriter("/Users/Alvaro/Desktop/data.json", "UTF-8");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                writer.println(jsonFile.toString());
                writer.close();
            }
        }
        else if ( evento.getSource() == quitar ) {
            System.out.print("Quitar: (" + lista.getSelectedIndex() + ") " + listaDirectorios.get(lista.getSelectedIndex()) + "\n");

            listaDirectorios.removeElementAt(lista.getSelectedIndex());
            lista.setModel( listaDirectorios );
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