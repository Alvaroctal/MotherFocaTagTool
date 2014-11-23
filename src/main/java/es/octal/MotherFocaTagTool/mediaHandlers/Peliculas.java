package main.java.es.octal.MotherFocaTagTool.mediaHandlers;

import main.java.es.octal.MotherFocaTagTool.mediaHandlers.peliculas.Pelicula;
import main.java.es.octal.MotherFocaTagTool.mediaHandlers.peliculas.Saga;
import main.java.org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Alvaro on 28/07/14.
 * MotherFocaTagTool
 */

public class Peliculas {

    private boolean noFail = true, showOnlyFails;
    private JSONObject json = new JSONObject();
    private JSONObject peliculas = new JSONObject();
    private JTextArea log;

    // Constructor

    public Peliculas(JTextArea log, Boolean showOnlyFails){

        this.log = log;
        this.showOnlyFails = showOnlyFails;
    }

    public void listar (String path) throws FileNotFoundException, UnsupportedEncodingException {

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        if (!showOnlyFails) {
            this.log.append("+ " + path + "\n | \n");
            this.log.setCaretPosition(this.log.getDocument().getLength());
        }
        try {
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

                        if (pelicula.verify()) {

                            if (!showOnlyFails) {
                                this.log.append(" | [done] " + pelicula.getName() + "\n");
                                this.log.setCaretPosition(this.log.getDocument().getLength());
                            }

                            saga.addMovie(pelicula);
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

                    this.peliculas.put(saga.getName(), saga.getJson());


                } else if (listOfFiles[i].isFile()) {

                    // Nueva pelicula

                    Pelicula pelicula = new Pelicula(listOfFiles[i]);

                    if (pelicula.verify()) {
                        this.peliculas.put(pelicula.getJson().getString("title"), pelicula.getJson());
                        if (!showOnlyFails) {
                            this.log.append(" | [done] " + pelicula.getName() + "\n");
                            this.log.setCaretPosition(this.log.getDocument().getLength());
                        }
                    } else {
                        if (this.noFail) {
                            this.log.append("\n");
                            this.noFail = false;
                        }

                        this.log.append("NO MATCH - (" + pelicula.getAbsolutePath() + ")\n");
                        this.log.setCaretPosition(this.log.getDocument().getLength());
                    }
                }

                this.log.update(this.log.getGraphics());
            }

            this.linkTree();
        }
        catch (java.lang.NullPointerException e) {
            this.log.append("[error] No se pudo acceder al directorio\n");
            this.noFail = false;
        }
    }

    private void linkTree(){
        this.json.put("noFail", this.noFail);
        this.json.put("Movies", this.peliculas);
    }

    // Get privates

    public JSONObject getJson(){
        return this.json;
    }
    public boolean getNoFail(){
        return this.noFail;
    }
}

