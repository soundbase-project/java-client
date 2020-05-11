package com.soundhive.controllers;

import com.soundhive.Router;
import com.soundhive.authentication.SessionHandler;

@FunctionalInterface
public interface InterfaceController {
    public void setContext(Router router, SessionHandler session);
}
