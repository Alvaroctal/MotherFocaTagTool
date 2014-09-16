package main.java.es.octal.MotherFocaTagTool.list;

import main.java.es.octal.MotherFocaTagTool.list.data.Pelicula;
import main.java.es.octal.MotherFocaTagTool.list.data.Saga;
import main.java.org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Alvaro on 28/07/14.
 * MotherFocaTagTool
 */

public class ListaPeliculas {

    private boolean noFail, showOnlyFails;
    private JSONObject json, peliculas;
    private JTextArea log;

    // Constructor

    public ListaPeliculas(JTextArea log, Boolean showOnlyFails){

        this.log = log;
        this.showOnlyFails = showOnlyFails;

    }

    public JSONObject creaIndice (String path, JSONObject json) throws FileNotFoundException, UnsupportedEncodingException {

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        if (! json.has("Peliculas")) {

            // El json no contiene ninguna lista de peliculas

            this.json = new JSONObject();
            this.peliculas = new JSONObject();
            this.json.put("Peliculas", this.peliculas);
            this.noFail = true;
        }
        else {

            // EL json contiene una lista de peliculas

            this.peliculas = json.getJSONObject("Peliculas");
            this.json.put("Peliculas", this.peliculas);
            this.noFail = json.getBoolean("noFail");
        }

        if (! showOnlyFails) {

            // Si el checkbox de mostrar log completo esta marcado

            this.log.append("+ " + path + "\n | \n");
            this.log.setCaretPosition(this.log.getDocument().getLength());
        }

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isDirectory()) {

                // Nueva saga

                Saga saga = new Saga(listOfFiles[i]);

                if (!showOnlyFails) {

                    this.log.append("+-+ " + saga.getName() + "\n");
                    this.log.setCaretPosition(this.log.getDocument().getLength());
                }

                for (int j = 0; j < saga.getList().length; j++) {

                    // Nueva pelicula

                    Pelicula pelicula = new Pelicula(saga.getList()[j]);

                    if (pelicula.verify()){

                        if (!showOnlyFails) {
                            this.log.append(" | [done] " + pelicula.getName() + "\n");
                            this.log.setCaretPosition(this.log.getDocument().getLength());
                        }

                        saga.addMovie(pelicula.getJson());
                    }
                    else {
                        if (this.noFail) {
                            this.log.append("\n");
                            this.noFail = false;
                        }

                        this.log.append("NO MATCH - (" + pelicula.getAbsolutePath() + ")\n");
                        this.log.setCaretPosition(this.log.getDocument().getLength());
                    }
                }

                // Linkamos la saga a la lista de peliculas

                this.peliculas.put(saga.getName(), saga.getJson());


            } else if (listOfFiles[i].isFile()) {

                // Nueva pelicula

                Pelicula pelicula = new Pelicula(listOfFiles[i]);

                if (pelicula.verify()){
                    this.peliculas.put(pelicula.getName(), pelicula.getJson());
                    if (!showOnlyFails) {
                        this.log.append(" | [done] " + pelicula.getName() + "\n");
                        this.log.setCaretPosition(this.log.getDocument().getLength());
                    }
                }
                else {
                    if (this.noFail) {
                        this.log.append("\n");
                        this.noFail = false;
                    }

                    this.log.append("NO MATCH - (" + pelicula.getAbsolutePath() + ")\n");
                    this.log.setCaretPosition(this.log.getDocument().getLength());
                }
            }
        }

        this.json.put("noFail", this.noFail);

        // Devolvemos el json al programa principal

        return this.json;
    }
}

