package main.java.es.octal.MotherFocaTagTool.mediaHandlers.series;

import main.java.org.json.JSONObject;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alvaro on 16/09/14.
 * MotherFocaTagTool
 */
public class Capitulo {

    private String name, title;
    private int number, numeroTemporada;
    private long size;
    private File file;
    private String patron;

    // Constructor

    public Capitulo(File file, String serie) {

        this.file = file;
        this.name = this.file.getName();
        this.patron = serie + " ([0-9]*)x([0-9]*) - ([^~.]*)";
    }

    public boolean verify(){

        Pattern pattern = Pattern.compile(this.patron);
        Matcher matcher = pattern.matcher(this.name);
        if (matcher.find()) {

            this.numeroTemporada = Integer.parseInt(matcher.group(1));
            this.number = Integer.parseInt(matcher.group(2));
            this.title = matcher.group(3);
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

        json.put("number", this.number);
        json.put("title", this.title);
        json.put("size", this.size);

        return json;
    }
    public String getAbsolutePath(){
        return this.file.getAbsolutePath();
    }
    public int getNumeroTemporada(){
        return this.numeroTemporada;
    }
    public int getNumber(){
        return this.number;
    }
    public String getTitle() {
        return this.title;
    }
}
