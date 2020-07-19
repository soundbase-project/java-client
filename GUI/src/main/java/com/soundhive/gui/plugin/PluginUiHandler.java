package com.soundhive.gui.plugin;

import com.soundhive.core.generic.Generic;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.function.Consumer;


public class PluginUiHandler{
    private final String uiPluginDir;
    private final List<PluginUIContainer> plugins;
    Consumer<List<PluginUIContainer>> navBarUpdate;
    final Consumer<String> messageLogger;
    final Consumer<Exception> exceptionLogger;

    public PluginUiHandler(final String uiPluginDir,
                           final Consumer<String> messageLogger,
                           final Consumer<Exception> exceptionLogger,
                           final Consumer<List<PluginUIContainer>> navBarUpdate)throws Exception {
        this.exceptionLogger = exceptionLogger;
        this.messageLogger = messageLogger;
        this.uiPluginDir = uiPluginDir;
        this.plugins = new ArrayList<>();
        this.navBarUpdate = navBarUpdate;
        loadPlugins();
    }

    private File[] getPluginsDirectory() throws IOException {
        File pluginDir = new File(this.uiPluginDir);
        if (!(pluginDir.exists() || pluginDir.mkdirs())) {
            throw new IOException("Unable to access or create Plugins directory.");
        }
        return new File(this.uiPluginDir).listFiles((file, name) -> name.endsWith(".jar"));
    }

    private void loadPlugins() throws Exception{
        final File[] pluginsDirectory = getPluginsDirectory();

        for (File pluginPath :
                pluginsDirectory) {
            PluginUIContainer pluginInstance = loadPlugin(pluginPath);
            this.plugins.add(pluginInstance);
        }
    }


    private PluginUIContainer loadPlugin(File path) throws MalformedURLException , ClassNotFoundException,
            NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        final URL[] urls = new URL[] {new URL("file:///" + path.getAbsolutePath())};
        final URLClassLoader child = new URLClassLoader(urls, PluginUiHandler.class.getClassLoader());

        final Class<?> plugin = Class.forName("Controller", true, child);

        PluginController castedPlugin = (PluginController) plugin.getConstructor().newInstance();
        boolean isValid = checkForMethods(castedPlugin, path);

        return new PluginUIContainer(castedPlugin, child, path, isValid);
    }


    private boolean checkForMethods(PluginController controller, File pluginPath) {
        return (checkForMethod(controller,"start", pluginPath)
                && checkForMethod(controller,"getViewName", pluginPath)
                && checkForMethod(controller,"getName", pluginPath));
    }

    public void HotLoadPlugin(File file) throws IOException, NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException, ClassNotFoundException{
        File dest = new File(this.uiPluginDir + '/' + file.getName());
        Generic.copyFileUsingStream(file, dest);
        this.plugins.add(loadPlugin(dest));
        navBarUpdate.accept(this.plugins);
    }

    private boolean checkForMethod(PluginController controller, String methodName,File pluginPath , Class<?>... parameterTypes) throws SecurityException {
        Method method = null;
        try {
            method = controller.getClass().getDeclaredMethod(methodName, parameterTypes);

        } catch (NoSuchMethodException | SecurityException e) {

            this.messageLogger.accept(String.format("Error on plugin \"%s\" : method \"%s\" is missing.\n\n",
                    pluginPath.getName(), methodName));
            this.exceptionLogger.accept(e);

        }
        return method != null;
    }

    public List<PluginUIContainer> getPlugins() {
        return this.plugins;
    }


    public void deletePlugin(final PluginUIContainer plugin) {
        plugin.delete();
        this.plugins.remove(plugin);
    }

}
