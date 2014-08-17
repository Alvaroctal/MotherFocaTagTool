package main.java.es.octal.MotherFocaTagTool.list.data;

import main.java.org.json.JSONObject;

import java.io.File;

/**
 * Created by Alvaro on 01/08/14.
 * MotherFocaTagTool
 */
public class Pelicula {

    // Nombres

    public String nombrePelicula;
    public String nombreSaga;

    // Directorios

    public String sagaDir;

    // Objetos File

    public File sagaFile;

    // Patron

    public String patron = "([0-9a-zA-Zá-ú-ñ\\- ]*) \\(([0-9]*)\\) \\[([0-9]*)\\] \\[(Dual|Cast|Vose)\\]";

    // Listas

    public File[] listaSaga;

    // Json

    public JSONObject jsonSaga;
    public JSONObject jsonPelicula;

    public Pelicula() {

        // Constructor nulo
    }
}
