package com.soundhive.core.conf;

import kong.unirest.Unirest;

import java.io.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class ConfHandler {
    Dictionary<String, String> parameters;


    public ConfHandler() throws  ConfigFileException{
        this.parameters = new Hashtable<>();
        File confFile = new File("conf/soundhive.conf");
        try {
                if (!confFile.exists()) {
                    if ((confFile.getParentFile().mkdirs() || confFile.getParentFile().exists())
                            && confFile.createNewFile()) {
                        writeDefaultConfFile(confFile);
                    } else {
                        throw  new ConfigFileException("Could not create configuration file.");
                    }
                }
        } catch (IOException e) {
            throw new ConfigFileException("System error while creating configuration file. Please check that you have access to this directory.", e);
        }
        readConfFile(confFile);

        Unirest.config().defaultBaseUrl(this.parameters.get("api_base_url"));
    }

    private void readConfFile(File file) throws ConfigFileException{
        try {
            FileReader reader = new FileReader(file);
            char[] buffer = new char[500];
            if (reader.read(buffer) > 0){
                String rawConf = new String(buffer).trim();
                String[] entries = rawConf.split(";");
                for (String entry:
                        entries) {
                    String[] keyValue = entry.split("=");
                    parameters.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new ConfigFileException("Something went wrong with the Config file.", e);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new ConfigFileException("Config file is not valid.", e);
        }



    }

    private void writeDefaultConfFile(File file) throws IOException{
        FileWriter writer = new FileWriter(file);
        writer.write("api_base_url=http://localhost:3000/;" +
                "token_directory=./auth/token;" +
                "ui_plugin_dir=./UIPlugins;" +
                "verbose=false;");
        writer.close();
    }

    public String getParam(String paramName) throws  MissingParamException{
        String res = this.parameters.get(paramName);
        if (res != null) {
            return res;
        }
        else {
            throw new MissingParamException("The Configuration file does not contain the property : \"" + paramName + "\".");
        }
    }




}
