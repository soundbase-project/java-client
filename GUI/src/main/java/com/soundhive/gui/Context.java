package com.soundhive.gui;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.authentication.UserProfileConsumer;
import com.soundhive.core.conf.ConfHandler;
import com.soundhive.core.conf.ConfHandler.VerboseLevel;
import com.soundhive.core.conf.MissingParamException;
import com.soundhive.gui.plugin.PluginUIContainer;
import com.soundhive.gui.plugin.PluginUiHandler;
import javafx.scene.image.Image;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class Context {

    private final ImageFetchingHandler picHandler;
    private final PluginUiHandler pluginHandler;
    private final ConfHandler conf;
    private final Router router;

    private SessionHandler session;
    private VerboseLevel verbose;

    private final UserProfileConsumer profileLoader;

    public Context(final Router router,
                   final BiConsumer<String, Image> profileLoader,
                   final Consumer<List<PluginUIContainer>> pluginsConsumer)  throws  Exception {

        this.router = router;
        this.conf = new ConfHandler();

        String baseMinioURL = this.getConf().getParam("minio_url");
        this.picHandler = new ImageFetchingHandler(baseMinioURL);

        this.profileLoader = ((username, profilePicUrl) -> profileLoader.accept(username, this.picHandler.getImage(profilePicUrl)));

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
                    this.verbose = VerboseLevel.SOFT;
                    this.router.issueDialog("Given verbose level is incorrect. Please set verbose as \"quiet\", \"soft\", or \"hard\".\n Verbose automatically set to \"soft\"");
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

    public ImageFetchingHandler getPicHandler() {
        return picHandler;
    }
}
