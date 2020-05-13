package com.soundhive.plugin;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginHandler {
    public static void loadPlugin(String path) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        final URL[] urls = new URL[] {new URL("file:///" + path)};
        final URLClassLoader child = new URLClassLoader(urls, PluginHandler.class.getClassLoader());

        final Class<?> plugin = Class.forName("Extension", true, child);
        Plugin extension = (Plugin) plugin.getConstructor().newInstance();
        extension.start();
    }
}
