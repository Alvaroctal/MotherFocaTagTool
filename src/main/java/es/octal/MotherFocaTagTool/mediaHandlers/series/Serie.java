package main.java.es.octal.MotherFocaTagTool.mediaHandlers.series;

import main.java.org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alvaro on 31/07/14.
 * MotherFocaTagTool
 */

public class Serie {

    // Nombres

    private String name;
    private File file;
    private List<Temporada> temporadas = new ArrayList();

    public Serie(File file) {

        // Constructor

        this.file = file;
        this.name = this.file.getName();
    }

    public void addTemporada(Temporada temporada){

        this.temporadas.add(temporada);
    }

    // Get privates

    public JSONObject getJson(){

        JSONObject json = new JSONObject();

        for(Temporada temporada : this.temporadas){
            json.put("season " + temporada.getNumber(), temporada.getJson());
        }
        return json;
    }
    public String getName(){
        return this.name;
    }
    public File[] getList(){
        return this.file.listFiles();
    }

}
