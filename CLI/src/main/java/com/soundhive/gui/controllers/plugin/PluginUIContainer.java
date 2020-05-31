package com.soundhive.gui.controllers.plugin;

import javafx.fxml.FXMLLoader;

import java.net.URLClassLoader;

public class PluginUIContainer {
    private final PluginController plugin;
    private final URLClassLoader classLoader;
    private final FXMLLoader view;

    public PluginUIContainer(PluginController plugin, URLClassLoader classLoader) {
        this.plugin = plugin;
        this.classLoader = classLoader;
        view = new FXMLLoader(classLoader.getResource(plugin.getViewName()));
        view.setController(plugin);

    }

    public String getName(){
        return this.plugin.getPluginName();

    }

    public FXMLLoader getView() {
        return this.view;
    }

    public PluginController getPlugin() {
        return this.plugin;
    }

}
