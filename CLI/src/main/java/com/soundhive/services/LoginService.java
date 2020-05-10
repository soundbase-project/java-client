package com.soundhive.services;

import javafx.beans.property.StringProperty;

public class LoginService {
    public final StringProperty username;

    public LoginService(StringProperty username) {
        this.username = username;
    }
}
