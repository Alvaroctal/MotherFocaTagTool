package main.java.es.octal.MotherFocaTagTool.mediaHandlers.peliculas;

import main.java.org.json.JSONObject;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alvaro on 01/08/14.
 * MotherFocaTagTool
 */
public class Pelicula {

    private String name;
    private File file;
    private JSONObject json;

    private String patron = "([0-9a-zA-Zá-ú-ñ\\- ]*) \\(([0-9]*)\\) \\[([0-9]*)\\] \\[(Dual|Cast|Vose)\\]";

    // Constructor

    public Pelicula(File file) {

        this.file = file;
        this.name = this.file.getName();
    }

    public boolean verify(){

        // Verifica el archivo

        Pattern pattern = Pattern.compile(this.patron);
        Matcher m = pattern.matcher(this.name);
        if (m.find()) {

            // Nueva pelicula

            this.json = new JSONObject();
            this.json.put("titulo", m.group(1));
            this.json.put("año", m.group(2));
            this.json.put("definicion", m.group(3));
            this.json.put("audio", m.group(4));
            this.json.put("size", this.file.length());

            return true;
        }

        return false;
    }

    // Get privates

    public String getName(){
        return this.name;
    }
    public JSONObject getJson(){
        return this.json;
    }
    public String getAbsolutePath(){
        return this.file.getAbsolutePath();
    }
}
