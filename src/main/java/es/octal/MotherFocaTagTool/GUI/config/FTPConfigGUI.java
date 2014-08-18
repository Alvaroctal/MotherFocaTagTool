package main.java.es.octal.MotherFocaTagTool.GUI.config;

import main.java.es.octal.MotherFocaTagTool.GUI.config.data.Config;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Alvaro on 17/08/14.
 * MotherFocaTagTool
 */

public class FTPConfigGUI extends JPanel implements ActionListener {

    // log

    JTextArea log;

    // Config

    Config config;

    // Paneles

    private JPanel panelFormularioSuperior, panelFormularioInferior;

    // Labels

    private JLabel serverLabel, userLabel, passLabel;

    // TextFields

    JTextField server, user;
    JPasswordField pass;

    // Botones

    private JButton guardar;

    public FTPConfigGUI(JTextArea log, Config config) throws IOException {

        // Log

        this.log = log;

        // Config

        this.config = config;

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

        // Mostramos los valores en los JTextfield

        server.setText(config.ftp.getServer());
        user.setText(config.ftp.getUser());
        pass.setText(config.ftp.getPass());

        log.append("[ftp] Se ha cargado la configuracion del ftp\n");



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

            config.ftp.edit(server.getText(), user.getText(), new String(pass.getPassword()));
            try {
                config.save();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

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
