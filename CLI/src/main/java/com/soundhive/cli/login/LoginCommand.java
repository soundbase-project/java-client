package com.soundhive.cli.login;

import com.soundhive.cli.SoundHiveCommand;
import com.soundhive.core.response.Response;
import picocli.CommandLine;

@CommandLine.Command(
        name = "login",
        description = "Login to the SoundHive platform."
)
public class LoginCommand implements Runnable {

    @CommandLine.Parameters(index = "1", paramLabel = "username", description = "User name")
    String username;

    @CommandLine.Parameters(index = "0", paramLabel = "password", description = "User password", interactive = true)
    String password;

    @CommandLine.Option(names = {"-sc","--stay-connected"}, description = "Connect automatically using a stored token")
    boolean stayConnected = false;



    @Override
    public void run()  {
        Response<?> result = SoundHiveCommand.getContext().getSession().openSession(this.username, this.password, this.stayConnected);
        switch (result.getStatus()) {
            case SUCCESS:
                System.out.println(String.format("Successfully logged in as %s!", SoundHiveCommand.getContext().getSession().getUsername()));
                break;
            case UNAUTHENTICATED:
                System.out.println("Wrong password or username. please try again.");
                break;
            case CONNECTION_FAILED:
                System.out.println("Could not connect the internet.");
                break;
            default:
                System.out.println("An error occurred.");
                SoundHiveCommand.getContext().log(result.getMessage());
                if (result.getException() != null) {
                    SoundHiveCommand.getContext().logException(result.getException());
                }

        }
    }
}
