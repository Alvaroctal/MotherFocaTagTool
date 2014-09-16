package main.java.es.octal.MotherFocaTagTool.mediaHandlers.peliculas;

import main.java.org.json.JSONObject;

import java.io.File;

/**
 * Created by Alvaro on 16/09/14.
 * MotherFocaTagTool
 */
public class Saga {

    private String name;
    private JSONObject json = new JSONObject();
    private File file;
    private int cuentaPeliculas;

    // Constructor

    public Saga(File fileSaga){

        this.file = fileSaga;
        this.name = this.file.getName();
    }

    public void addMovie(JSONObject pelicula){
        this.json.put(pelicula.getString("titulo"), pelicula);
        this.cuentaPeliculas++;
    }

    // Get privates

    public String getName(){
        return this.name;
    }
    public File[] getList(){
        return this.file.listFiles();
    }
    public JSONObject getJson(){
        this.json.put("cuentaPeliculas", this.cuentaPeliculas);
        return this.json;
    }
}
