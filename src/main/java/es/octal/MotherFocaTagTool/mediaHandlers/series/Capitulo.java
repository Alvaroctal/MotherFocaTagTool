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

    private String name, titulo;
    private File file;
    private JSONObject json;
    private String patron;
    private int numero, numeroTemporada;

    // Constructor

    public Capitulo(File file, String serie) {

        this.file = file;
        this.name = this.file.getName();
        this.patron = serie + " ([0-9]*)x([0-9]*) - ([^~.]*)";;
    }

    public boolean verify(){

        Pattern pattern = Pattern.compile(this.patron);
        Matcher matcher = pattern.matcher(this.name);
        if (matcher.find()) {

            json = new JSONObject();

            this.numeroTemporada = Integer.parseInt(matcher.group(1));
            this.numero = Integer.parseInt(matcher.group(2));
            this.titulo = matcher.group(3);

            this.json.put("numero", this.numero);
            this.json.put("titulo", this.titulo);
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
    public int getNumeroTemporada(){
        return this.numeroTemporada;
    }
    public int getNumero(){
        return this.numero;
    }
    public String getTitulo() {
        return this.titulo;
    }
}
