package main.java.es.octal.MotherFocaTagTool.mediaHandlers;

import main.java.es.octal.MotherFocaTagTool.mediaHandlers.series.Capitulo;
import main.java.es.octal.MotherFocaTagTool.mediaHandlers.series.Serie;
import main.java.es.octal.MotherFocaTagTool.mediaHandlers.series.Temporada;
import main.java.org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Alvaro on 31/07/14.
 * MotherFocaTagTool
 */

public class Series {

    private boolean noFail, showOnlyFails;
    private JSONObject json = new JSONObject();
    private JSONObject series = new JSONObject();
    private JTextArea log;
    private int numeroSeries;

    // Constructor

    public Series(JTextArea log, Boolean showOnlyFails){

        this.log = log;
        this.showOnlyFails = showOnlyFails;

    }

    public void listar (String path) throws FileNotFoundException, UnsupportedEncodingException {

        boolean showTitles = false;

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        if (! showOnlyFails) {

            // Si el checkbox de mostrar log completo esta marcado

            this.log.append("+ " + path + "\n | \n");
            this.log.setCaretPosition(this.log.getDocument().getLength());
        }
        try {
            for (int i = 0; i < listOfFiles.length; i++) {

                if (listOfFiles[i].isDirectory()) {

                    // Nueva serie

                    Serie serie = new Serie(listOfFiles[i]);

                    if (! showOnlyFails){
                        this.log.append("+-+ " + serie.getName() + "\n");
                    }

                    for (int j = 0; j < serie.getList().length; j++) {

                        if (serie.getList()[j].isDirectory()) {

                            // Nueva temporada

                            Temporada temporada = new Temporada(serie.getList()[j], serie.getName());

                            if (temporada.verify()) {

                                if (! showOnlyFails){
                                    this.log.append(" |  + Temporada " + temporada.getNumero());
                                    if (! showTitles){
                                        this.log.append(" - " + temporada.getList().length + " capitulos");
                                    }
                                    this.log.append("\n");
                                }

                                for (int k = 0; k < temporada.getList().length; k++) {

                                    if (temporada.getList()[k].isFile()){

                                        // Nuevo Capitulo

                                        Capitulo capitulo = new Capitulo(temporada.getList()[k], serie.getName());

                                        if (capitulo.verify()) {

                                            if (! showOnlyFails && showTitles){
                                                this.log.append(" |   |- Capitulo " + capitulo.getNumero() + ": " + capitulo.getTitulo() + "\n");
                                            }

                                            if (temporada.getNumero() != capitulo.getNumeroTemporada()) {

                                                this.log.append("[warn] Directorio incorrecto (" + capitulo.getAbsolutePath() + ")\n");
                                            }

                                            temporada.addCapitulo(capitulo.getJson());
                                        }
                                        else {

                                            // Un capitulo no cumple el patron

                                            this.log.append("NO MATCH - (" + capitulo.getAbsolutePath() + ")\n");
                                            this.noFail = false;
                                        }
                                    }
                                    else {

                                        // Un directorio dentro del directorio de una temporada

                                        this.log.append("[warn] ¿extra? (" + temporada.getList()[k].getPath() + ")\n");
                                    }
                                }

                                serie.addTemporada(temporada.getJson(), temporada.getNumero());
                            }
                            else {

                                // Una temporada no cumple el patron

                                this.log.append("SEASON NO MATCH - (" + temporada.getAbsolutePath() + ")\n");
                                this.noFail = false;
                            }
                        }
                        else {

                            // Un archivo dentro del directorio de una serie

                            this.log.append("[warn] Sin temporada (" + serie.getList()[j].getPath() + ")\n");
                        }
                    }
                    this.series.put(serie.getName(), serie.getJson());
                    this.numeroSeries++;
                }
                else {

                    // Un archivo dentro del directorio de series

                    this.log.append("[warn] Sin serie (" + listOfFiles[i].getAbsolutePath() + ")\n");
                }
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
        this.json.put("numeroSeries", this.numeroSeries);
        this.json.put("Series", this.series);
    }

    // Get privates

    public JSONObject getJson(){
        return this.json;
    }
    public boolean getNoFail(){
        return this.noFail;
    }
}
