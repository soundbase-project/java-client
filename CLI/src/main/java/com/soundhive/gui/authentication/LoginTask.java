package com.soundhive.gui.authentication;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import javafx.concurrent.Task;



public class LoginTask extends Task<Response<Void>> {

    private final String username;
    private final String password;
    private final boolean stayConnected;

    private final boolean autoConnect;

    private final SessionHandler session;

    public LoginTask(final String username, final String password, final boolean stayConnected, final SessionHandler session, final boolean autoConnect) {
        this.username = username;
        this.password = password;
        this.stayConnected = stayConnected;
        this.session = session;
        this.autoConnect = autoConnect;
    }

    protected Response<Void> call() {
        if (session.checkForToken() ) {
            return this.session.loadUserProfile();
        }
        else if (!autoConnect){
            return this.session.openSession(username, password, stayConnected);
        }
        else return new Response<>(Response.Status.UNAUTHENTICATED, "No Token, could not auto-connect.");

    }
}
