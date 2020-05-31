package com.soundhive.gui.controllers;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class SamplesController extends Controller {

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
    protected void start() {

    }
}
