package com.soundhive.authentication;

import com.soundhive.response.Response;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ServiceConfigurationError;

public class LoginWithTokenService extends Service<Response<Void>> {
    final SessionHandler session;

    public LoginWithTokenService(SessionHandler session) {
        this.session = session;
    }

    @Override
    protected Task<Response<Void>> createTask() {
        return new LoginWithTokenTask(session);
    }
}
