package com.soundhive.gui.controllers;

import com.jfoenix.controls.*;
import com.soundhive.core.Enums;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class SamplesController extends Controller {

    @FXML
    private JFXListView<HBox> lvSamples;

    @FXML
    private JFXTextField tfTitle;

    @FXML
    private JFXTextArea taDescription;

    @FXML JFXComboBox<Enums.License> cbLicense;

    @FXML JFXComboBox<Enums.Visibility> cbVisibility;

    @FXML JFXButton btFile;

    @FXML JFXButton btUpload;



    @FXML
    private void initialize() {

    }

    @Override
    protected void start() {

    }
}
