package main.java.es.octal.MotherFocaTagTool.list.data;

import main.java.org.json.JSONObject;

import java.io.File;

/**
 * Created by Alvaro on 31/07/14.
 * java
 */

public class Serie {

    // Nombres

    public String nombreSerie;
    public String nombreTemporada;  // Se refiere al nombre del directorio que contiene la temporada
    public String nombreCapitulo;   // Se refiere al nombre del archivo del capitulo

    // Directorios

    public String serieDir;
    public String temporadaDir;
    public String capituloDir;

    // Objetos File

    public File serieFile;
    public File temporadaFile;
    public File capituloFile;

    // Patrones

    public String patronTemporada;
    public String patronCapitulo;

    // Listas

    public File[] listaTemporadas;
    public File[] listaCapitulos;

    // Json

    public JSONObject jsonSerie;
    public JSONObject jsonTemporada;
    public JSONObject jsonCapitulo;

    public Serie(String nombreSerie) {

        // Constructor

        this.nombreSerie = nombreSerie;

        patronTemporada = nombreSerie + " - Temporada ([0-9]*) \\(([0-9]*)\\) - ([0-9]*|SD) \\[(Dual|Cast|Vose)\\]";
        patronCapitulo = nombreSerie + " ([0-9]*)x([0-9]*) - ([^~.]*)";
    }
}
