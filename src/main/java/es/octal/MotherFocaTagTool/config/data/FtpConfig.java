package main.java.es.octal.MotherFocaTagTool.config.data;

import main.java.org.json.JSONObject;

/**
 * Created by Alvaro on 18/08/14.
 * MotherFocaTagTool
 */

public class FtpConfig extends JSONObject{
    private String server, user, pass;

    public FtpConfig(String server, String user, String pass){
        this.server = server;
        this.user = user;
        this.pass = pass;
        linkTree();
    }
    public void edit(String server, String user, String pass){
        this.server = server;
        this.user = user;
        this.pass = pass;
        linkTree();
    }
    public String getServer(){
        return this.server;
    }
    public String getUser(){
        return this.user;
    }
    public String getPass(){
        return this.pass;
    }
    private void linkTree(){
        this.put("server", server);
        this.put("user", user);
        this.put("pass", pass);
    }
}
