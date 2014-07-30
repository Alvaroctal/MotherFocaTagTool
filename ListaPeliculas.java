package MotherFocaTagTool;

import MotherFocaTagTool.org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alvaro on 28/07/14.
 * Listador
 */

public class ListaPeliculas {

    public void Listador(){

        // Construtor nulo

    }

    public JSONObject listaPeliculas(JTextArea textArea, String path, JSONObject jsonFile) throws FileNotFoundException, UnsupportedEncodingException {

        // Variables temporales de bedug

        boolean showOnlyFails = false, falloDetectado = false;

        String subPath, nombreArchivo, nombreDirectorio;

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        // Json

        JSONObject peliculas;

        if (! jsonFile.has("Peliculas")) {

            // El json no contiene ninguna lista de peliculas

            peliculas = new JSONObject();
            jsonFile.put("Peliculas", peliculas);
        }
        else{

            // EL json contiene una lista de peliculas

            peliculas = jsonFile.getJSONObject("Peliculas");
            jsonFile.put("Peliculas", peliculas);
        }

        // Patron

        String patron = "([0-9a-zA-Zá-ú-ñ\\- ]*) \\(([0-9]*)\\) \\[([0-9]*)\\] \\[(Dual|Cast|Vose)\\]";

        if (! showOnlyFails) {

            // Si el checkbox de mostrar log completo esta marcado

            textArea.append("\n+ " + path + "\n | \n");
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isDirectory()){

                // Parte para tratar directorios

                nombreDirectorio = listOfFiles[i].getName();

                if (! showOnlyFails) {

                    textArea.append("+-+ " + nombreDirectorio + "\n");
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                }

                // Creamos la saga

                LinkedHashMap saga = new LinkedHashMap();

                // Nos deplazamos al directorio

                subPath = path + nombreDirectorio + "/";

                File subFolder = new File(subPath);
                File[] listOfFilesInFolder = subFolder.listFiles();

                // Listar archivos del directorio

                for (int j = 0; j < listOfFilesInFolder.length; j++) {

                    // Parte para tratar ficheros

                    nombreArchivo = listOfFilesInFolder[j].getName();

                    // Crear el objeto del patron

                    Pattern pattern = Pattern.compile(patron);

                    // Inserta el string en el patron

                    Matcher m = pattern.matcher(nombreArchivo);

                    // Comprueba si el string cuadra en el patron

                    if (m.find( )) {

                        // Creamos un nuevo array, una pelicula

                        LinkedHashMap pelicula = new LinkedHashMap();
                        pelicula.put("año", m.group(2));
                        pelicula.put("definicion", m.group(3));
                        pelicula.put("audio", m.group(4));

                        // Linkamos la pelicula a la saga

                        saga.put(m.group(1), pelicula);

                        if (! showOnlyFails) {

                            // Si el checkbox de mostrar log completo esta marcado

                            textArea.append(" |   | [done] " + m.group(1) + "\n");
                            textArea.setCaretPosition(textArea.getDocument().getLength());
                        }

                    } else {

                        // Si no cuadra

                        if (! falloDetectado) {
                            textArea.append("\n");
                            falloDetectado = true;
                        }

                        textArea.append("NO MATCH - (" + nombreArchivo + ")\n");// - (" + patron + ")\n");
                        textArea.setCaretPosition(textArea.getDocument().getLength());
                    }
                }

                // Linkamos la saga a la lista de peliculas

                peliculas.put(nombreDirectorio, saga);


            }
            else if (listOfFiles[i].isFile()) {

                // Parte para tratar ficheros

                nombreArchivo = listOfFiles[i].getName();

                // Crear el objeto del patron

                Pattern pattern = Pattern.compile(patron);

                // Inserta el string en el patron

                Matcher m = pattern.matcher(nombreArchivo);

                // Comprueba si el string cuadra en el patron

                if (m.find( )) {

                    // Creamos un nuevo array, una pelicula

                    LinkedHashMap pelicula = new LinkedHashMap();
                    pelicula.put("año", m.group(2));
                    pelicula.put("definicion", m.group(3));
                    pelicula.put("audio", m.group(4));

                    peliculas.put(m.group(1), pelicula);
                    if (! showOnlyFails) {
                        textArea.append(" | [done] " + m.group(1) + "\n");
                        textArea.setCaretPosition(textArea.getDocument().getLength());
                    }
                } else {

                    if (! falloDetectado) {
                        textArea.append("\n");
                        falloDetectado = true;
                    }

                    textArea.append("[warn] NO MATCH - (" + nombreArchivo + ")\n"); // - (" + patron + ")\n");
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                }
            }
        }

        // Devolvemos el json al programa principal

        return jsonFile;
    }
}

