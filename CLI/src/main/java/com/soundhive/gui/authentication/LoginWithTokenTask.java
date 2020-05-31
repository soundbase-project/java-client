package com.soundhive.gui.authentication;


import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import javafx.concurrent.Task;

public class LoginWithTokenTask extends Task<Response<Void>> {

    private final SessionHandler session;

    public LoginWithTokenTask(final SessionHandler session) {
        this.session = session;
    }
    @Override
    protected Response<Void> call() {
        return session.loadUserProfile();
    }
}
