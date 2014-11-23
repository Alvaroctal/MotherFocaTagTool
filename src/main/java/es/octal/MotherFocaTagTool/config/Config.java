package main.java.es.octal.MotherFocaTagTool.config;

import main.java.es.octal.MotherFocaTagTool.config.data.FtpConfig;
import main.java.es.octal.MotherFocaTagTool.config.data.MediaConfig;
import main.java.org.json.JSONArray;
import main.java.org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Alvaro on 18/08/14.
 * MotherFocaTagTool
 */

public class Config extends JSONObject{

    // Externas

    private JTextArea log;
    private String configFileDir;

    // JSON

    private JSONObject storedConfig;

    // Ramas

    public FtpConfig ftp;
    public MediaConfig peliculas, series;

    // File

    private File configFile;

    // Constructor

    public Config(JTextArea log, String configFileDir) throws IOException {

        //------------------------------------------------------------------------------
        //  Variables externas
        //------------------------------------------------------------------------------

        this.log = log;
        this.configFileDir = configFileDir;
        this.configFile = new File(configFileDir);

        //------------------------------------------------------------------------------
        //  Comprobacion de configuracion previa
        //------------------------------------------------------------------------------

        if(configFile.exists() && !configFile.isDirectory()) {

            // EL fichero de configuracion existe

            storedConfig = new JSONObject(new String(Files.readAllBytes(Paths.get(configFileDir))));

            // FTP

            if (storedConfig.has("ftp")) {

                // Existe configuracion previa sobre ftp

                ftp = new FtpConfig(
                        storedConfig.getJSONObject("ftp").getString("server"),
                        storedConfig.getJSONObject("ftp").getString("user"),
                        storedConfig.getJSONObject("ftp").getString("pass")
                );
                log.append("[ftp] Se ha cargado la configuracion\n");
            }
            else{
                this.createFTP();
                log.append("[ftp] Se ha creado la configuracion\n");
            }

            // Peliculas

            if (storedConfig.has("peliculas") && storedConfig.getJSONObject("peliculas").has("dirs")) {

                // Existe configuracion previa sobre peliculas

                peliculas = new MediaConfig(
                        storedConfig.getJSONObject("peliculas").getJSONArray("dirs"),
                        System.getProperty("user.home") + File.separator + "peliculas.json",
                        "peliculas.json");
                log.append("[peliculas] Se ha cargado la configuracion\n");

            }
            else{
                this.createPeliculas();
                log.append("[peliculas] Se ha creado la configuracion\n");
            }

            // Series

            if (storedConfig.has("series") && storedConfig.getJSONObject("series").has("dirs")) {

                // Existe configuracion previa sobre series

                series = new MediaConfig(
                        storedConfig.getJSONObject("series").getJSONArray("dirs"),
                        System.getProperty("user.home") + File.separator + "series.json",
                        "series.json");
                log.append("[series] Se ha cargado la configuracion\n");
            }
            else{
                this.createSeries();
                log.append("[series] Se ha creado la configuracion\n");
            }
        }
        else {

            // El fichero de configuracion no existe

            createFTP();
            createPeliculas();
            createSeries();

            log.append("[config] Se ha creado la configuracion\n");
        }

        this.save();
    }
    private void createFTP(){
        ftp = new FtpConfig("", "", "");
    }
    private void createPeliculas(){
        peliculas = new MediaConfig(
                new JSONArray(),
                System.getProperty("user.home") + File.separator + "peliculas.json",
                "peliculas.json");
    }
    private void createSeries(){
        series = new MediaConfig(
                new JSONArray(),
                System.getProperty("user.home") + File.separator + "series.json",
                "series.json");
    }
    private void linkTree(){
        this.put("ftp", ftp);
        this.put("peliculas", peliculas);
        this.put("series", series);
    }
    public void save() throws FileNotFoundException, UnsupportedEncodingException {

        this.linkTree();

        PrintWriter writer = new PrintWriter(configFileDir, "UTF-8");
        writer.println(this.toString(4));
        writer.close();
    }
}
