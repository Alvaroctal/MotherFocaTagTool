package main.java.es.octal.MotherFocaTagTool.mediaHandlers.series;

import main.java.org.json.JSONObject;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alvaro on 16/09/14.
 * MotherFocaTagTool
 */
public class Temporada{

    private String name;
    private File file;
    private JSONObject json;
    private String patron;
    private int numero, numeroCapitulos;

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

            // Nueva temporada

            json = new JSONObject();

            this.numero = Integer.parseInt(matcher.group(1));

            this.json.put("año", matcher.group(2));
            this.json.put("definicion", matcher.group(3));
            this.json.put("audio", matcher.group(4));

            return true;
        }

        return false;
    }

    public void addCapitulo(JSONObject capitulo) {
        this.json.put("capitulo " + capitulo.getInt("numero"), capitulo);
        this.numeroCapitulos++;
    }

    // Get privates

    public JSONObject getJson(){
        this.json.put("numeroCapitulos", this.numeroCapitulos);
        return this.json;
    }
    public int getNumero(){
        return numero;
    }
    public File[] getList(){
        return this.file.listFiles();
    }
    public String getAbsolutePath(){
        return this.file.getAbsolutePath();
    }
}
