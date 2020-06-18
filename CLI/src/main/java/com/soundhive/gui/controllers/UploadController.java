package com.soundhive.gui.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class UploadController extends Controller {


    @FXML
    private JFXTextField tfNbTracks;

    @FXML
    private void initialize() { // TODO use special new component
        this.tfNbTracks.setOnAction(e -> {
           purifyNumberField(this.tfNbTracks);
        });
    }

    @Override
    protected void start() {

    }

    private void purifyNumberField(JFXTextField field) {
        if (!field.getText().matches("^[0-9]+$")) {

            String text = field.getText();
            String result = text.replaceAll("[^0-9]*","" );
            field.setText(result);
        }
    }

}
