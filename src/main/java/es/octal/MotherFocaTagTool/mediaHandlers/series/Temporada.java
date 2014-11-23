package main.java.es.octal.MotherFocaTagTool.mediaHandlers.series;

import main.java.org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alvaro on 16/09/14.
 * MotherFocaTagTool
 */
public class Temporada{

    private String name, resolution, audio;
    private int number, year;
    private File file;
    private String patron;
    private List<Capitulo> capitulos = new ArrayList();

    // Construtor

    public Temporada(File file, String serie){

        this.file = file;
        this.name = this.file.getName();
        this.patron = serie + " - Temporada ([0-9]*) \\(([0-9]*)\\) - ([0-9]*|SD) \\[(Dual|Cast|Vose)\\]";
    }

    public boolean verify(){

        Pattern pattern = Pattern.compile(this.patron);
        Matcher matcher = pattern.matcher(this.name);
        if (matcher.find()) {

            this.number = Integer.parseInt(matcher.group(1));
            this.year = Integer.parseInt(matcher.group(2));
            this.resolution = matcher.group(3);
            this.audio = matcher.group(4);

            return true;
        }

        return false;
    }

    public void addCapitulo(Capitulo capitulo) {
        this.capitulos.add(capitulo);
    }

    // Get privates

    public JSONObject getJson(){

        JSONObject json = new JSONObject();

        json.put("number", this.number);
        json.put("year", this.year);
        json.put("resolution", this.resolution);
        json.put("audio", this.audio);

        for(Capitulo capitulo : this.capitulos){
            json.put("chapter " + capitulo.getNumber(), capitulo.getJson());
        }
        return json;
    }
    public int getNumber(){
        return number;
    }
    public File[] getList(){
        return this.file.listFiles();
    }
    public String getAbsolutePath(){
        return this.file.getAbsolutePath();
    }
}
