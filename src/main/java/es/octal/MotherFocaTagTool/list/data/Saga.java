package main.java.es.octal.MotherFocaTagTool.list.data;

import main.java.org.json.JSONObject;

import java.io.File;

/**
 * Created by Alvaro on 16/09/14.
 * MotherFocaTagTool
 */
public class Saga {

    private String name;
    private JSONObject json = new JSONObject();
    private File fileSaga;
    private int cuentaPeliculas;

    // Constructor

    public Saga(File fileSaga){

        this.fileSaga = fileSaga;
        this.name = this.fileSaga.getName();
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
        return this.fileSaga.listFiles();
    }
    public JSONObject getJson(){
        this.json.put("cuentaPeliculas", this.cuentaPeliculas);
        return this.json;
    }
}
