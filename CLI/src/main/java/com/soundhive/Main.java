package com.soundhive;

import com.soundhive.plugin.PluginHandler;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        try {
            String url = "/home/vagahbond/Documents/ESGI/Projet Annuel/jfx_client/java_client_1224/plugin_hello/target/plugin_hello-1.0-SNAPSHOT.jar";
            System.out.println(new File(url).exists());
            PluginHandler.loadPlugin(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        App.main(args);


    }
}
