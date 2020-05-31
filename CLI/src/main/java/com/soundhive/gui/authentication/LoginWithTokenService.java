package com.soundhive.gui.authentication;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

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
