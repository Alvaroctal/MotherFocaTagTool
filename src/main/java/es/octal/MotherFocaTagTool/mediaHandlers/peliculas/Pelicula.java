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

    private String name, title, resolution, audio;
    private int year;
    private long size;
    private File file;

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

            this.title = m.group(1);
            this.year = Integer.parseInt(m.group(2));
            this.resolution = m.group(3);
            this.audio = m.group(4);
            this.size = this.file.length();

            return true;
        }

        return false;
    }

    // Get privates

    public String getName(){
        return this.name;
    }
    public JSONObject getJson(){

        JSONObject json = new JSONObject();

        json.put("title", this.title);
        json.put("year", this.year);
        json.put("resolution", this.resolution);
        json.put("audio", this.audio);
        json.put("size", this.size);

        return json;
    }
    public String getAbsolutePath(){
        return this.file.getAbsolutePath();
    }
    public String getTitle(){
        return this.title;
    }
}
