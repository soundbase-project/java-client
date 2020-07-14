package com.soundhive.cli;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.conf.ConfHandler;
import com.soundhive.core.conf.ConfHandler.VerboseLevel;
import com.soundhive.core.conf.ConfigFileException;
import com.soundhive.core.conf.MissingParamException;


public class Context {
    private final SessionHandler session;

    private final ConfHandler conf;

    private final VerboseLevel verbose;

    public Context() throws ConfigFileException, MissingParamException {
        this.conf = new ConfHandler();

        this.verbose = determineVerbose(conf.getParam("verbose").toLowerCase());

        this.session = new SessionHandler(conf.getParam("token_directory"));
    }

    private VerboseLevel determineVerbose(String verbose) {
        switch (verbose) {
            case "quiet":
                return VerboseLevel.QUIET;
            case "soft":
                return  VerboseLevel.SOFT;
            case "hard":
                return VerboseLevel.HARD;
            default:
                System.out.println("Given verbose level is incorrect. Please set verbose as \"quiet\", \"soft\", or \"hard\".\n Verbose automatically set to \"soft\"");
                return VerboseLevel.SOFT;

        }
    }

    public SessionHandler getSession() {
        return session;
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
