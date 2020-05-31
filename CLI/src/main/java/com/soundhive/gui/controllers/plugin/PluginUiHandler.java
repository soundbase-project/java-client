package com.soundhive.gui.controllers.plugin;

import com.soundhive.core.Globals;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class PluginUiHandler {

    private File[] getPluginsDirectory() throws IOException {
        File pluginDir = new File(Globals.UI_PLUGINS_DIR);
        if (!(pluginDir.exists() || pluginDir.mkdirs())) {
            throw new IOException("Unable to access or create Plugins directory.");
        }
        return new File(Globals.UI_PLUGINS_DIR).listFiles((f, n) -> n.endsWith(".jar"));
    }

    public List<PluginUIContainer> loadPlugins() throws Exception{

        final File[] pluginsDirectory = getPluginsDirectory();
        List<PluginUIContainer> plugins = new ArrayList<>();
        for (File plugin :
                pluginsDirectory) {
            try {
                plugins.add(loadPlugin(plugin));
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Unable to load plugin : " + plugin.getName());
            }

        }
        return plugins;
    }


    private PluginUIContainer loadPlugin(File path) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException{
        final URL[] urls = new URL[] {new URL("file:///" + path.getAbsolutePath())};
        final URLClassLoader child = new URLClassLoader(urls, PluginUiHandler.class.getClassLoader());

        final Class<?> plugin = Class.forName("TestController", true, child);

        return new PluginUIContainer((PluginController) plugin.getConstructor().newInstance(), child);
    }
}
