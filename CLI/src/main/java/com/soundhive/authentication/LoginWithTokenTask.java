package com.soundhive.authentication;

import com.soundhive.authentication.SessionHandler.LoginStatus;
import javafx.concurrent.Task;

public class LoginWithTokenTask extends Task<LoginStatus> {

    private SessionHandler session;

    public LoginWithTokenTask(SessionHandler session) {
        this.session = session;
    }
    @Override
    protected LoginStatus call() {
        return session.loginWithToken();
    }
}
