package MotherFocaTagTool.list;

import MotherFocaTagTool.list.data.Pelicula;
import MotherFocaTagTool.org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alvaro on 28/07/14.
 * Listador
 */

public class ListaPeliculas {

    public void ListaPeliculas(){

        // Construtor nulo

    }

    public JSONObject creaIndice (JTextArea log, String path, JSONObject jsonPeliculas) throws FileNotFoundException, UnsupportedEncodingException {

        // Variables temporales de debug

        boolean showOnlyFails = false, falloDetectado = false;

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        // Json

        JSONObject peliculas;

        if (! jsonPeliculas.has("Peliculas")) {

            // El json no contiene ninguna lista de peliculas

            peliculas = new JSONObject();
            jsonPeliculas.put("Peliculas", peliculas);
        }
        else{

            // EL json contiene una lista de peliculas

            peliculas = jsonPeliculas.getJSONObject("Peliculas");
            jsonPeliculas.put("Peliculas", peliculas);
        }

        if (! showOnlyFails) {

            // Si el checkbox de mostrar log completo esta marcado

            log.append("\n+ " + path + "\n | \n");
            log.setCaretPosition(log.getDocument().getLength());
        }

        for (int i = 0; i < listOfFiles.length; i++) {

            Pelicula pelicula = new Pelicula();

            if (listOfFiles[i].isDirectory()){

                // Parte para tratar directorios

                pelicula.nombreSaga = listOfFiles[i].getName();

                if (! showOnlyFails) {

                    log.append("+-+ " + pelicula.nombreSaga + "\n");
                    log.setCaretPosition(log.getDocument().getLength());
                }

                // Creamos la saga

                pelicula.jsonSaga = new JSONObject();

                // Nos deplazamos al directorio

                pelicula.sagaDir = path + File.separator + pelicula.nombreSaga;
                pelicula.sagaFile = new File(pelicula.sagaDir);
                pelicula.listaSaga = pelicula.sagaFile.listFiles();

                // Listar archivos del directorio

                for (int j = 0; j < pelicula.listaSaga.length; j++) {

                    // Parte para tratar ficheros

                    pelicula.nombrePelicula = pelicula.listaSaga[j].getName();

                    // Crear el objeto del patron

                    Pattern pattern = Pattern.compile(pelicula.patron);
                    Matcher m = pattern.matcher(pelicula.nombrePelicula);
                    if (m.find( )) {

                        // Creamos un nuevo array, una pelicula

                        pelicula.jsonPelicula = new JSONObject();
                        pelicula.jsonPelicula.put("titulo", m.group(1));
                        pelicula.jsonPelicula.put("año", m.group(2));
                        pelicula.jsonPelicula.put("definicion", m.group(3));
                        pelicula.jsonPelicula.put("audio", m.group(4));

                        // Linkamos la pelicula a la saga

                        pelicula.jsonSaga.put(m.group(1), pelicula.jsonPelicula);
                        pelicula.jsonSaga.put("nombre", pelicula.nombreSaga);

                        if (! showOnlyFails) {

                            // Si el checkbox de mostrar log completo esta marcado

                            log.append(" |   | [done] " + m.group(1) + "\n");
                            log.setCaretPosition(log.getDocument().getLength());
                        }

                    } else {

                        // Si no cuadra

                        if (! falloDetectado) {
                            log.append("\n");
                            falloDetectado = true;
                        }

                        log.append("NO MATCH - (" + pelicula.listaSaga[j].getAbsolutePath() + ")\n");// - (" + patron + ")\n");
                        log.setCaretPosition(log.getDocument().getLength());
                    }
                }

                // Linkamos la saga a la lista de peliculas

                peliculas.put(pelicula.nombreSaga, pelicula.jsonSaga);


            }
            else if (listOfFiles[i].isFile()) {

                // Parte para tratar ficheros

                pelicula.nombrePelicula = listOfFiles[i].getName();

                // Crear el objeto del patron

                Pattern pattern = Pattern.compile(pelicula.patron);
                Matcher m = pattern.matcher(pelicula.nombrePelicula);
                if (m.find( )) {

                    // Creamos un nuevo array, una pelicula

                    pelicula.jsonPelicula = new JSONObject();
                    pelicula.jsonPelicula.put("titulo", m.group(1));
                    pelicula.jsonPelicula.put("año", m.group(2));
                    pelicula.jsonPelicula.put("definicion", m.group(3));
                    pelicula.jsonPelicula.put("audio", m.group(4));

                    peliculas.put(m.group(1), pelicula.jsonPelicula);
                    if (! showOnlyFails) {
                        log.append(" | [done] " + m.group(1) + "\n");
                        log.setCaretPosition(log.getDocument().getLength());
                    }
                } else {

                    if (! falloDetectado) {
                        log.append("\n");
                        falloDetectado = true;
                    }

                    log.append("NO MATCH - (" + listOfFiles[i].getAbsolutePath() + ")\n");
                    log.setCaretPosition(log.getDocument().getLength());
                }
            }
        }

        // Devolvemos el json al programa principal

        return jsonPeliculas;
    }
}

