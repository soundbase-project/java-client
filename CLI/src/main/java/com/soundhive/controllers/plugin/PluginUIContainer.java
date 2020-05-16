package com.soundhive.controllers.plugin;

import java.net.URL;
import java.net.URLClassLoader;

public class PluginUIContainer {
    private final IPluginUiController plugin;
    private final URLClassLoader classLoader;

    public PluginUIContainer(IPluginUiController plugin, URLClassLoader classLoader) {
        this.plugin = plugin;
        this.classLoader = classLoader;
    }

    public String getName(){
        return this.plugin.getPluginName();

    }

    public URL getView() {
        return classLoader.getResource(plugin.getViewName());
    }

    public IPluginUiController getPlugin() {
        return this.plugin;
    }

}
