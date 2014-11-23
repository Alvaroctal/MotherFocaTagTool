package main.java.es.octal.MotherFocaTagTool.mediaHandlers.peliculas;

import main.java.org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alvaro on 16/09/14.
 * MotherFocaTagTool
 */
public class Saga {

    private String name;
    private File file;
    private List<Pelicula> peliculas = new ArrayList();

    // Constructor

    public Saga(File fileSaga){

        this.file = fileSaga;
        this.name = this.file.getName();
    }

    public void addMovie(Pelicula pelicula){
        this.peliculas.add(pelicula);
    }

    // Get privates

    public String getName(){
        return this.name;
    }
    public File[] getList(){
        return this.file.listFiles();
    }
    public JSONObject getJson(){

        JSONObject json = new JSONObject();

        json.put("name", this.name);

        for(Pelicula pelicula : this.peliculas){
            json.put(pelicula.getTitle(), pelicula.getJson());
        }
        return json;
    }
}
