package main.java.es.octal.MotherFocaTagTool.list;

import main.java.es.octal.MotherFocaTagTool.list.data.Serie;
import main.java.org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alvaro on 31/07/14.
 * java
 */

public class ListaSeries {

    public void ListaSeries(){

        // Construtor nulo

    }

    public JSONObject creaIndice (JTextArea log, String path, JSONObject jsonSeries, Boolean showOnlyFails) throws FileNotFoundException, UnsupportedEncodingException {

        // Variables temporales de debug

        boolean noFail = true, showTitles = false;

        String nombreArchivo;

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        // Json

        JSONObject series;

        if (! jsonSeries.has("Series")) {

            // El json no contiene ninguna lista de peliculas

            series = new JSONObject();
            jsonSeries.put("Series", series);
        }
        else{

            // EL json contiene una lista de peliculas

            series = jsonSeries.getJSONObject("Series");
            jsonSeries.put("Series", series);
        }

        if (! showOnlyFails) {

            // Si el checkbox de mostrar log completo esta marcado

            log.append("+ " + path + "\n | \n");
            log.setCaretPosition(log.getDocument().getLength());
        }

        try{
            for (int i = 0; i < listOfFiles.length; i++) {

                if (listOfFiles[i].isDirectory()) {

                    // Creamos una nueva serie

                    Serie serie = new Serie(listOfFiles[i].getName());

                    if (! showOnlyFails){
                        log.append("+-+ " + serie.nombreSerie + "\n");
                    }

                    // Creamos un nuevo arbol, una serie

                    serie.jsonSerie = new JSONObject();
                    series.put(serie.nombreSerie, serie.jsonSerie);

                    // Nos deplazamos a la serie

                    serie.serieDir = path + File.separator + serie.nombreSerie;
                    serie.serieFile = new File(serie.serieDir);
                    serie.listaTemporadas = serie.serieFile.listFiles();

                    for (int j = 0; j < serie.listaTemporadas.length; j++) {

                        if (serie.listaTemporadas[j].isDirectory()) {

                            // Tratar temporadas

                            serie.nombreTemporada = serie.listaTemporadas[j].getName();

                            // Comprobamos el patron

                            Pattern patternTemporadas = Pattern.compile(serie.patronTemporada);
                            Matcher matcherTemporadas = patternTemporadas.matcher(serie.nombreTemporada);
                            if (matcherTemporadas.find()) {

                                // Creamos un nuevo arbol, una temporada

                                serie.jsonTemporada = new JSONObject();
                                serie.jsonSerie.put("temporada " + matcherTemporadas.group(1), serie.jsonTemporada);

                                // Rellenamos campos

                                serie.jsonTemporada.put("año", matcherTemporadas.group(2));
                                serie.jsonTemporada.put("definicion", matcherTemporadas.group(3));
                                serie.jsonTemporada.put("audio", matcherTemporadas.group(4));

                                // Nos deplazamos a la temporada

                                serie.temporadaDir = serie.serieDir + File.separator + serie.nombreTemporada;
                                serie.temporadaFile = new File(serie.temporadaDir);
                                serie.listaCapitulos = serie.temporadaFile.listFiles();

                                if (! showOnlyFails){
                                    log.append(" |  + Temporada " + matcherTemporadas.group(1));
                                    if (! showTitles){
                                        log.append(" - " + serie.listaCapitulos.length + " capitulos");
                                    }
                                    log.append("\n");
                                }

                                for (int k = 0; k < serie.listaCapitulos.length; k++) {

                                    if (serie.listaCapitulos[k].isFile()){
                                        // Tratar capitulos

                                        serie.nombreCapitulo = serie.listaCapitulos[k].getName();

                                        // Comprobamos el patron

                                        Pattern patternCapitulos = Pattern.compile(serie.patronCapitulo);
                                        Matcher matcherCapitulos = patternCapitulos.matcher(serie.nombreCapitulo);
                                        if (matcherCapitulos.find()) {

                                            // Creamos un nuevo arbol, un capitulo

                                            serie.jsonCapitulo = new JSONObject();
                                            serie.jsonTemporada.put(matcherCapitulos.group(2), serie.jsonCapitulo);

                                            if (! showOnlyFails && showTitles){
                                                log.append(" |   |- Capitulo " + matcherCapitulos.group(2) + ": " + matcherCapitulos.group(3) + "\n");
                                            }

                                            // Rellenamos campos

                                            serie.capituloDir = serie.temporadaDir + File.separator + serie.nombreCapitulo;
                                            serie.capituloFile = new File(serie.capituloDir);

                                            if (! matcherTemporadas.group(1).toString().equals(matcherCapitulos.group(1))) {

                                                // Si un capitulo no esta en su temporada

                                                log.append("[warn] Directorio incorrecto (" + serie.capituloFile.getAbsolutePath() + ")\n");
                                            }

                                            serie.jsonCapitulo.put("numero", matcherCapitulos.group(2));
                                            serie.jsonCapitulo.put("titulo", matcherCapitulos.group(3));
                                        } else {

                                            // Un capitulo no cumple el patron

                                            log.append("NO MATCH - (" + serie.nombreCapitulo + ")\n");
                                            noFail = false;
                                        }
                                    }
                                    else if (serie.listaCapitulos[k].isDirectory()){

                                        // Un directorio dentro de una temporada

                                        log.append("[warn] ¿extra? (" + serie.listaCapitulos[k].getPath() + ")\n");
                                    }
                                }
                                serie.jsonTemporada.put("capitulos", serie.listaCapitulos.length);
                            } else {

                                // Una temporada no cumple el patron

                                log.append("SEASON NO MATCH - (" + serie.serieDir + File.separator + serie.nombreTemporada + ")\n");
                                noFail = false;
                            }
                        } else if (serie.listaTemporadas[j].isFile()){

                            // Un archivo dentro de una serie

                            log.append("[warn] Sin temporada (" + serie.listaTemporadas[j].getPath() + ")\n");
                        }
                    }
                    serie.jsonSerie.put("temporadas", serie.listaTemporadas.length);
                }
                else if (listOfFiles[i].isFile()) {

                    // Un archivo donde las series

                    log.append("[warn] Sin serie (" + listOfFiles[i].getAbsolutePath() + ")\n");
                }
            }
        }
        catch (java.lang.NullPointerException e){
            log.append("[error] No se pudo acceder al directorio\n");
            noFail = false;
        }

        jsonSeries.put("noFail", noFail);

        // Devolvemos el json al programa principal

        return jsonSeries;
    }
}
