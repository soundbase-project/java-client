package com.soundhive.controllers;

import com.soundhive.Router;
import com.soundhive.authentication.SessionHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MainController {
    //create service var

    private Router router;
    private SessionHandler session;

    @FXML
    private Label lbSession;

    @FXML
    private ImageView ivSession;

    @FXML
    private AnchorPane appContent;

    @FXML
    public void initialize() {
        this.router = new Router(appContent);
        this.session = new SessionHandler(lbSession.textProperty());
        router.goTo("Login", controller -> controller.setContext(router, session));
    }

    public void setRouter(final Router router) {
        this.router = router;
    }

}
