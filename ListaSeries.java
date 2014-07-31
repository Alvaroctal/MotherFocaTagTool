package MotherFocaTagTool;

import MotherFocaTagTool.org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alvaro on 31/07/14.
 * MotherFocaTagTool
 */

public class ListaSeries {

    public void ListaSeries(){

        // Construtor nulo

    }

    public JSONObject creaIndice (String path, JSONObject jsonSeries) throws FileNotFoundException, UnsupportedEncodingException {

        // Variables temporales de debug

        boolean showOnlyFails = false, falloDetectado = false;

        String nombreArchivo;

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        // Json

        JSONObject peliculas;

        if (! jsonSeries.has("Series")) {

            // El json no contiene ninguna lista de peliculas

            peliculas = new JSONObject();
            jsonSeries.put("Series", peliculas);
        }
        else{

            // EL json contiene una lista de peliculas

            peliculas = jsonSeries.getJSONObject("Series");
            jsonSeries.put("Series", peliculas);
        }

        if (! showOnlyFails) {

            // Si el checkbox de mostrar log completo esta marcado

            //textArea.append("\n+ " + path + "\n | \n");
            //textArea.setCaretPosition(textArea.getDocument().getLength());
        }

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isDirectory()) {

                // Creamos una nueva serie

                Serie serie = new Serie(listOfFiles[i].getName());

                // Creamos un nuevo arbol, una serie

                serie.jsonSerie = new JSONObject();
                jsonSeries.put(serie.nombreSerie, serie.jsonSerie);

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
                            serie.jsonSerie.put(matcherTemporadas.group(1), serie.jsonTemporada);

                            // Rellenamos campos

                            serie.jsonTemporada.put("Temporada", matcherTemporadas.group(1));
                            serie.jsonTemporada.put("Año", matcherTemporadas.group(2));
                            serie.jsonTemporada.put("Definicion", matcherTemporadas.group(3));
                            serie.jsonTemporada.put("Audio", matcherTemporadas.group(4));

                            // Nos deplazamos a la temporada

                            serie.temporadaDir = serie.serieDir + File.separator + serie.nombreTemporada;
                            serie.temporadaFile = new File(serie.temporadaDir);
                            serie.listaCapitulos = serie.temporadaFile.listFiles();

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

                                        // Rellenamos campos

                                        serie.capituloDir = serie.temporadaDir + File.separator + serie.nombreCapitulo;
                                        serie.capituloFile = new File(serie.capituloDir);

                                        if (matcherTemporadas.group(1) != matcherTemporadas.group(1)) {

                                            // Si un capitulo no esta en su temporada

                                            //textArea.append("[warn] Directorio incorrecto (" + serie.capituloFile.getAbsolutePath() + ")\n");
                                        }

                                        serie.jsonCapitulo.put("Numero", matcherTemporadas.group(2));
                                        serie.jsonCapitulo.put("Titulo", matcherTemporadas.group(3));
                                    } else {

                                        // Un capitulo no cumple el patron

                                        System.out.print("NO MATCH - (" + serie.temporadaDir + File.separator + serie.nombreCapitulo + ")\n");
                                    }
                                }
                                else if (serie.listaCapitulos[k].isDirectory()){

                                    // Un directorio dentro de una temporada

                                    //textArea.append("[warn] ¿extra? (" + serie.listaCapitulos[k].getPath() + ")\n");
                                }
                            }
                        } else {

                            // Una temporada no cumple el patron

                            System.out.print("SEASON NO MATCH - (" + serie.serieDir + File.separator + serie.nombreTemporada + ")\n");
                        }
                    } else if (serie.listaTemporadas[j].isFile()){

                        // Un archivo dentro de una serie

                        //textArea.append("[warn] Sin temporada (" + serie.listaTemporadas[j].getPath() + ")\n");
                    }
                }
            }
            else if (listOfFiles[i].isFile()) {

                // Un archivo donde las series

                //textArea.append("[warn] Sin serie (" + listOfFiles[i].getAbsolutePath() + ")\n");
            }
        }

        // Devolvemos el json al programa principal

        return jsonSeries;
    }

    // Clase principal para debugear mientras se hace la GUI

    public static void main(String[] args) throws Exception {
        // Json

        JSONObject jsonSeries = new JSONObject();

        ListaSeries listaSeries = new ListaSeries();

        jsonSeries = listaSeries.creaIndice("/Volumes/AlmacenAlfa/Series", jsonSeries);

        System.out.println("\n\n" + jsonSeries.toString());
    }
}
