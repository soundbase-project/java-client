package com.soundhive.cli.login;

import com.soundhive.core.authentication.SessionHandler;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "login",
        description = "Login to the SoundHive platform."
)
public class LoginCommand implements Callable<SessionHandler> {

    @CommandLine.Parameters(index = "0", paramLabel = "password", description = "User password", interactive = true)
    String password;

    @CommandLine.Parameters(index = "1", paramLabel = "username", description = "User name")
    String username;



    @Override
    public SessionHandler call()  {
        System.out.println(username);
        System.out.println(password);
        return new SessionHandler("auth/token", (username1, profilePicUrl) -> System.out.println("logged in !"));
    }
}
