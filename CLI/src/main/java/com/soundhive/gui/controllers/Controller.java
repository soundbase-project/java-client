package com.soundhive.gui.controllers;

import com.soundhive.gui.Router;
import com.soundhive.core.authentication.SessionHandler;

public abstract class Controller {
    private SessionHandler session;
    private Router router;

    public void setContextAndStart(Router router, SessionHandler session){
        this.router = router;
        this.session = session;
        start();
    }

    protected Router getRouter() {
        return router;
    }

    protected SessionHandler getSession() {
        return session;
    }

    protected abstract void start();


}
