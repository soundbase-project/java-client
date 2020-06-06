package com.soundhive.gui.plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PluginUiHandler {
    private final String uiPluginDir;
    public PluginUiHandler(final String uiPluginDir) {
        this.uiPluginDir = uiPluginDir;
    }

    private File[] getPluginsDirectory() throws IOException {
        File pluginDir = new File(this.uiPluginDir);
        if (!(pluginDir.exists() || pluginDir.mkdirs())) {
            throw new IOException("Unable to access or create Plugins directory.");
        }
        return new File(this.uiPluginDir).listFiles((f, n) -> n.endsWith(".jar"));
    }

    public List<PluginUIContainer> loadPlugins(boolean verbose) throws Exception{
        final File[] pluginsDirectory = getPluginsDirectory();
        List<PluginUIContainer> plugins = new ArrayList<>();
        for (File pluginPath :
                pluginsDirectory) {
            PluginUIContainer pluginInstance = loadPlugin(pluginPath);
            if (checkForMethods(pluginInstance.getPlugin(),"start", verbose)
                    && checkForMethods(pluginInstance.getPlugin(),"getViewName", verbose)
                    && checkForMethods(pluginInstance.getPlugin(),"getName", verbose)) {
                plugins.add(pluginInstance);
            }
            else {
                if (verbose) {
                    System.err.println("Could not load plugin : " + pluginPath);
                }
            }

        }
        return plugins;
    }


    private PluginUIContainer loadPlugin(File path) throws Exception{
        final URL[] urls = new URL[] {new URL("file:///" + path.getAbsolutePath())};
        final URLClassLoader child = new URLClassLoader(urls, PluginUiHandler.class.getClassLoader());

        final Class<?> plugin = Class.forName("Controller", true, child);

        return new PluginUIContainer((PluginController) plugin.getConstructor().newInstance(), child, path);
    }

    private boolean checkForMethods(PluginController controller, String methodName, boolean verbose, Class<?>... parameterTypes) throws NoSuchElementException, SecurityException {
        Method method = null;
        try {
            method = controller.getClass().getDeclaredMethod(methodName, parameterTypes);

        } catch (NoSuchMethodException | SecurityException e) {
            if (verbose) {
                e.printStackTrace();
            }

        }
        return method != null;
    }
}
