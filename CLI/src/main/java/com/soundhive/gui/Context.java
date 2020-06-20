package com.soundhive.gui;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.authentication.UserProfileConsumer;
import com.soundhive.core.conf.ConfHandler;
import com.soundhive.core.conf.MissingParamException;
import com.soundhive.gui.plugin.PluginUIContainer;
import com.soundhive.gui.plugin.PluginUiHandler;

import java.util.List;
import java.util.function.Consumer;

public class Context {
    public enum VerboseLevel{
        QUIET,
        SOFT,
        HARD
    }

    private VerboseLevel verbose;
    private SessionHandler session;
    private final Router router;
    private final ConfHandler conf;
    private final PluginUiHandler pluginHandler;


    private final UserProfileConsumer profileLoader;

    public Context(final Router router, final UserProfileConsumer profileLoader, final Consumer<List<PluginUIContainer>> pluginsConsumer)  throws  Exception {
        this.conf = new ConfHandler();
        this.profileLoader = profileLoader;
        this.router = router;
        initSession();
        initVerbose();
        this.pluginHandler = new PluginUiHandler(this.getConf().getParam("plugin_ui_dir"), this::log, this::logException, pluginsConsumer);

    }


    private void initSession() throws MissingParamException{
        String tokenDirectory = conf.getParam("token_directory");


        this.session = new SessionHandler(tokenDirectory, this.profileLoader);
    }

    private void initVerbose() {
        // verbose is an optional parameter
        try {
            switch (conf.getParam("verbose").toLowerCase()) {
                case "quiet":
                    this.verbose = VerboseLevel.QUIET;
                    break;
                case "soft":
                    this.verbose = VerboseLevel.SOFT;
                    break;
                case "hard":
                    this.verbose = VerboseLevel.HARD;
                    break;
                default:
                    this.router.issueDialog("Given verbose level is incorrect. Please set verbose as \"quiet\", \"soft\", or \"hard\".");
            }
        } catch (MissingParamException e){
            //default
            this.verbose = VerboseLevel.SOFT;
        }
    }

    public SessionHandler getSession() {
        return session;
    }

    public Router getRouter() {
        return router;
    }

    public ConfHandler getConf() {
        return conf;
    }


    public PluginUiHandler getPluginHandler() {
        return this.pluginHandler;
    }


    public void log(final String message) {
        if (this.verbose != VerboseLevel.QUIET) {
            System.out.println(message);
        }
    }

    public void logException(final Throwable e) {
        if (this.verbose == VerboseLevel.HARD) {
            e.printStackTrace();
        }
    }

}
