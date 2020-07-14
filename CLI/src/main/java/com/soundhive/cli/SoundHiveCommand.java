package com.soundhive.cli;

import com.soundhive.cli.login.LoginCommand;
import picocli.CommandLine;

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
    public static void main(String[] args) {

        CommandLine.run(new SoundHiveCommand(), args);
    }

    @Override
    public void run() {
        System.out.println("SoundHive Command Line Interface. \n" +
                "--help" );
    }
}
