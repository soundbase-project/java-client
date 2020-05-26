package com.soundhive.authentication;

import com.soundhive.response.Response;
import javafx.concurrent.Task;



public class LoginTask extends Task<Response<Void>> {

    private final String username;
    private final String password;
    private final boolean stayConnected;

    private final SessionHandler session;

    public LoginTask(final String username, final String password, final boolean stayConnected, final SessionHandler session) {
        this.username = username;
        this.password = password;
        this.stayConnected = stayConnected;
        this.session = session;
    }

    protected Response<Void> call() {
        return this.session.openSession(username, password, stayConnected);
    }
}
