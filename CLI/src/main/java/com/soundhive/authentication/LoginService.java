package com.soundhive.authentication;

import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import kong.unirest.json.JSONObject;


public class LoginService extends Service<JSONObject> {

    private final StringProperty usernameProperty;
    private final StringProperty passwordProperty;

    public LoginService(final StringProperty usernameProperty, final StringProperty passwordProperty) {
        this.usernameProperty = usernameProperty;
        this.passwordProperty = passwordProperty;
    }

    protected Task<JSONObject> createTask() {
        return new LoginTask(usernameProperty.getValueSafe(), passwordProperty.getValueSafe());
    }
}
