package com.soundhive.controllers;

import com.soundhive.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {
    private Router router;

    @FXML private TextField tfUsername;
    @FXML private TextField tfPassword;
    @FXML private Button btLogin;



    @FXML
    public void initialize() {

    }

    public void setRouter(Router router) {
        this.router = router;
    }
}
