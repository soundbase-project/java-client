package com.soundhive.authentication;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ServiceConfigurationError;

public class LoginWithTokenService extends Service<SessionHandler.LoginStatus> {
    final SessionHandler session;

    public LoginWithTokenService(SessionHandler session) {
        this.session = session;
    }

    @Override
    protected Task<SessionHandler.LoginStatus> createTask() {
        return new LoginWithTokenTask(session);
    }
}
