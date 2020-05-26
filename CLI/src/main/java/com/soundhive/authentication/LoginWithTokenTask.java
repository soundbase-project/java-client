package com.soundhive.authentication;

import com.soundhive.response.Response;
import javafx.concurrent.Task;

public class LoginWithTokenTask extends Task<Response<Void>> {

    private SessionHandler session;

    public LoginWithTokenTask(SessionHandler session) {
        this.session = session;
    }
    @Override
    protected Response<Void> call() {
        return session.loadUserProfile();
    }
}
