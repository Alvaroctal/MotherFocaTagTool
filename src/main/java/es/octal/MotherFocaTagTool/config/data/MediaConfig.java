package main.java.es.octal.MotherFocaTagTool.config.data;

import main.java.org.json.JSONArray;
import main.java.org.json.JSONObject;

import javax.swing.*;

/**
 * Created by Alvaro on 18/08/14.
 * MotherFocaTagTool
 */

public class MediaConfig extends JSONObject {
    private String filePath, ftpFilePath;
    private JSONArray dirs;

    private DefaultListModel dirsList;

    public MediaConfig(JSONArray dirs, String filePath, String ftpFilePath){
        this.dirs = dirs;
        this.filePath = filePath;
        this.ftpFilePath = ftpFilePath;
        linkTree();
    }
    public void edit(JSONArray dirs){
        this.dirs = dirs;
        linkTree();
    }
    public DefaultListModel getDirsList(){

        dirsList = new DefaultListModel();

        for (int i = 0; i < dirs.length(); i++) {
            dirsList.addElement(dirs.getString(i));
        }
        return dirsList;
    }
    public String getfilePath(){
        return this.filePath;
    }
    public String getFtpFilePath(){
        return this.ftpFilePath;
    }
    private void linkTree(){
        this.put("dirs", dirs);
        this.put("filePath", filePath);
        this.put("ftpFilePath", ftpFilePath);
    }
}
