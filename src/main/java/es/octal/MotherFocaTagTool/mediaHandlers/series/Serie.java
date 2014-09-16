package main.java.es.octal.MotherFocaTagTool.mediaHandlers.series;

import main.java.org.json.JSONObject;

import java.io.File;

/**
 * Created by Alvaro on 31/07/14.
 * MotherFocaTagTool
 */

public class Serie {

    // Nombres

    private String name;
    private JSONObject json = new JSONObject();
    private File file;
    private int numeroTemporadas;

    public Serie(File file) {

        // Constructor

        this.file = file;
        this.name = this.file.getName();
    }

    public void addTemporada(JSONObject temporada, int numero){

        this.json.put("temporada " + numero, temporada);
        this.numeroTemporadas++;
    }

    // Get privates

    public JSONObject getJson(){
        this.json.put("numeroTemporadas", this.numeroTemporadas);
        return this.json;
    }
    public String getName(){
        return this.name;
    }
    public File[] getList(){
        return this.file.listFiles();
    }

}
