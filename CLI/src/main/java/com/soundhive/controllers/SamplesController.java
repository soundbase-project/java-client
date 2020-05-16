package com.soundhive.controllers;

import com.jfoenix.controls.JFXListView;
import com.soundhive.Router;
import com.soundhive.authentication.SessionHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class SamplesController implements IUiController {
    private Router router;
    private SessionHandler session;

    @FXML
    private JFXListView<HBox> lvSamples;

    @FXML
    private void initialize() {
        this.lvSamples.setEditable(true);
        for (int i = 0; i < 100; i++) {
            HBox box = new HBox();
            box.getChildren().addAll(new Label("sample name"), new Label("views : " +i));
        }
    }
    @Override
    public void setContext(Router router, SessionHandler session) {
        this.router = router;
        this.session = session;

    }
}
