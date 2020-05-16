package com.soundhive.controllers;

import com.soundhive.Router;
import com.soundhive.authentication.SessionHandler;
import javafx.fxml.FXML;

public class StatsController implements IUiController {
    private Router router;
    private  SessionHandler session;


    @FXML
    public void initialize() {

    }


    @Override
    public void setContext(Router router, SessionHandler session) {
        this.router = router;
        this.session = session;
    }
}
