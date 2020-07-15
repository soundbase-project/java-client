package com.soundhive.cli.login;

import com.soundhive.cli.SoundHiveCommand;
import picocli.CommandLine;

@CommandLine.Command(
        name = "logout",
        description = "Log out from SoundHive and destroy session token"
)
public class LogoutCommand implements Runnable {
    @Override
    public void run() {
        SoundHiveCommand.getContext().getSession().destroySession();
        System.out.println("Successfully logged out !");
    }
}
