package main.java.es.octal.MotherFocaTagTool.GUI.config;

import main.java.org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Alvaro on 17/08/14.
 * MotherFocaTagTool
 */

public class FTPConfigGUI extends JPanel implements ActionListener {

    // log

    JTextArea log;

    // Paneles

    private JPanel panelFormularioSuperior, panelFormularioInferior;

    // Labels

    private JLabel serverLabel, userLabel, passLabel;

    // TextFields

    JTextField server, user;
    JPasswordField pass;

    // Botones

    private JButton guardar;

    // File

    File configFile;
    String configFileDir;

    // Json

    JSONObject jsonConfig, jsonFTPConfig;

    public FTPConfigGUI(JTextArea log, String configFileDir) throws IOException {

        // Log

        this.log = log;

        // Config

        this.configFileDir = configFileDir;
        configFile = new File(configFileDir);

        //------------------------------------------------------------------------------
        //  Paneles
        //------------------------------------------------------------------------------

        panelFormularioSuperior = new JPanel();
        panelFormularioInferior = new JPanel();

        // Alineaciones

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        panelFormularioSuperior.setLayout(new BoxLayout(panelFormularioSuperior, BoxLayout.X_AXIS));
        panelFormularioInferior.setLayout(new BoxLayout(panelFormularioInferior, BoxLayout.X_AXIS));

        //------------------------------------------------------------------------------
        //  TextFields
        //------------------------------------------------------------------------------

        // User

        userLabel = new JLabel("User: ");
        panelFormularioSuperior.add(userLabel);
        user = new JTextField();
        panelFormularioSuperior.add(user);
        userLabel.setLabelFor(user);

        // Server

        serverLabel = new JLabel("Server: ");
        panelFormularioSuperior.add(serverLabel);
        server = new JTextField();
        panelFormularioSuperior.add(server);
        serverLabel.setLabelFor(server);

        this.add(panelFormularioSuperior);

        // Pass

        passLabel = new JLabel("Pass: ");
        panelFormularioInferior.add(passLabel);
        pass = new JPasswordField();
        panelFormularioInferior.add(pass);
        passLabel.setLabelFor(pass);

        // Guardar

        guardar = new JButton("Guardar");
        guardar.addActionListener(this);
        panelFormularioInferior.add(guardar);

        this.add(panelFormularioInferior);

        //------------------------------------------------------------------------------
        //  Comprobacion de configuracion previa
        //------------------------------------------------------------------------------

        if(configFile.exists() && !configFile.isDirectory()) {

            // El fichero de configuracion existe

            jsonConfig = new JSONObject(new String(Files.readAllBytes(Paths.get(configFileDir))));

            if (jsonConfig.has("ftp")) {

                // El fichero de configuracion contiene configuracion sobre peliculas

                jsonFTPConfig = jsonConfig.getJSONObject("ftp");

                // Mostramos los valores en los JTextfield

                server.setText(jsonFTPConfig.getString("server"));
                user.setText(jsonFTPConfig.getString("user"));
                pass.setText(jsonFTPConfig.getString("pass"));

                log.append("[ftp] Se ha cargado la configuracion del ftp\n");

            }
        }
        else {

            // El fichero de configuracion no existe, se creara

            log.append("[ftp] No se encontró fichero de configuracion\n");

            JSONObject jsonFTPConfig = new JSONObject();
            JSONObject jsonConfig = new JSONObject();

            jsonFTPConfig.put("server", server.getText());
            jsonFTPConfig.put("user", user.getText());
            jsonFTPConfig.put("pass", pass.getPassword());

            jsonConfig.put("ftp", jsonFTPConfig);

            PrintWriter writer = null;
            try {
                writer = new PrintWriter(configFileDir, "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            writer.println(jsonConfig.toString());
            writer.close();
        }
    }

    //------------------------------------------------------------------------------
    //  Escuchadores
    //------------------------------------------------------------------------------

    public void actionPerformed(ActionEvent evento) {

        //------------------------------------------------------------------------------
        //  Escuhador de guardar
        //------------------------------------------------------------------------------

        if (evento.getSource() == guardar) {

            // Guardar

            jsonFTPConfig = new JSONObject();
            jsonConfig = new JSONObject();

            jsonFTPConfig.put("server", server.getText());
            jsonFTPConfig.put("user", user.getText());
            jsonFTPConfig.put("pass", new String(pass.getPassword()));

            jsonConfig.put("ftp", jsonFTPConfig);

            PrintWriter writer = null;
            try {
                writer = new PrintWriter(configFileDir, "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            writer.println(jsonConfig.toString());
            writer.close();

            log.append("[ftp] Configuracion del FTP actualizada\n");
        }

        //------------------------------------------------------------------------------
        //  Escuchador de else
        //------------------------------------------------------------------------------

        else{
            log.append("[warn] No existe accion asociada al boton\n");
        }
    }
}
