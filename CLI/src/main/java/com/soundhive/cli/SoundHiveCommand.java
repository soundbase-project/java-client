package com.soundhive.cli;

import com.soundhive.cli.login.LoginCommand;
import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.conf.ConfHandler;
import com.soundhive.core.conf.ConfigFileException;
import com.soundhive.core.conf.MissingParamException;
import picocli.CommandLine;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.concurrent.Callable;

@CommandLine.Command(
        subcommands = {
            LoginCommand.class
        },
        mixinStandardHelpOptions = true,
        version = "SoundHive CLI 0.1",
        description = "Welcome to Soundhive's CLI utility."
)
public class SoundHiveCommand implements Runnable {

    private static Context context;

    public SoundHiveCommand() {
        try {
            context = new Context();
        }
        catch (ConfigFileException e) {
            System.out.println("Could not load Config file. See stacktrace ?(y/n)");
            if (System.console().readLine().equals("y")) {
                e.printStackTrace();
            }
            System.exit(-1);
        }
        catch (MissingParamException e) {
            System.out.println("An error occurred with the configuration file during the booting up of the app : \n " + e.getMessage());
            System.out.println("Could not load Config file. See stacktrace ?(y/n)");
            if (System.console().readLine().equals("y")) {
                e.printStackTrace();
            }
            System.exit(-1);
        }
        if (!context.getSession().checkForToken()) {
            context.log("You are not currently logged in. Please login using `soundhive login <username> [--stay-connected]`.");
        }

    }

    public static void main(String[] args) {
        CommandLine.run(new SoundHiveCommand(), args);
    }

    @Override
    public void run() {
        System.out.println("SoundHive Command Line Interface. \n" +
                "to see commands, use --help." );
    }

    public static Context getContext(){
        return context;
    }
}
