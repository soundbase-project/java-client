package com.soundhive.gui.plugin;

import javafx.fxml.FXMLLoader;

import java.io.File;
import java.net.URLClassLoader;

public class PluginUIContainer {
    private final PluginController plugin;
    private final FXMLLoader view;
    private final boolean isValid; // TODO

    private final File jarArchive;

    public PluginUIContainer(PluginController plugin, URLClassLoader classLoader, File jarArchive, boolean isValid) {
        this.plugin = plugin;
        this.isValid = isValid;
        view = new FXMLLoader(classLoader.getResource(plugin.getViewName()));
        view.setController(plugin);
        this.jarArchive = jarArchive;
    }

    public FXMLLoader getView() {
        return this.view;
    }

    public PluginController getPlugin() {
        return this.plugin;
    }



    public boolean delete() {
        return this.jarArchive.delete();
    }

    public boolean isValid() {
        return isValid;
    }
}
