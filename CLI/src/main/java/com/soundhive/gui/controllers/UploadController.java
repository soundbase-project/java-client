package com.soundhive.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class UploadController extends Controller {


    @FXML private JFXTextField tfTitle;
    @FXML private JFXListView<AnchorPane> lvTracks;
    @FXML private JFXButton btAddTrack;
    @FXML private JFXButton btRemoveTrack;
    @FXML private JFXButton btCoverFile;

    @FXML
    private void initialize() { // TODO use special new component
        //this.tfNbTracks.setOnAction(e -> purifyNumberField(this.tfNbTracks));
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
