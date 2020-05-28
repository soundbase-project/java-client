package com.soundhive.controllers;

import com.jfoenix.controls.JFXTextField;
import com.soundhive.Router;
import com.soundhive.authentication.SessionHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UploadController extends Controller {


    @FXML
    private JFXTextField tfNbTracks;

    @FXML
    private void initialize() {
        this.tfNbTracks.setOnAction(e -> {
            System.out.println("purifying input");
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
