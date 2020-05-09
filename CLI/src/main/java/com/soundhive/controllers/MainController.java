package com.soundhive.controllers;

import com.soundhive.Router;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MainController {
    //create service var

    private Router router;

//    @FXML
//    private Button btStats;

    @FXML
    private AnchorPane appContent;

    @FXML
    public void initialize() {
        this.router = new Router(appContent);
        router.<LoginController>goTo("Login", controller -> controller.setRouter(router));
//        btStats.setOnMouseClicked(event -> {
//            System.out.println("clicked");
//        });
    }

    public void setRouter(final Router router) {
        this.router = router;
    }

}
