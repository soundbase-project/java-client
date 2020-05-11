package com.soundhive.authentication;

import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import kong.unirest.json.JSONObject;


public class LoginService extends Service<SessionHandler.LoginStatus> {

    private final StringProperty usernameProperty;
    private final StringProperty passwordProperty;
    private final SessionHandler session;

    public LoginService(final StringProperty usernameProperty, final StringProperty passwordProperty, final SessionHandler session) {
        this.usernameProperty = usernameProperty;
        this.passwordProperty = passwordProperty;
        this.session = session;
    }

    protected Task<SessionHandler.LoginStatus> createTask() {
        return new LoginTask(usernameProperty.getValueSafe(), passwordProperty.getValueSafe(), this.session);
    }
}
