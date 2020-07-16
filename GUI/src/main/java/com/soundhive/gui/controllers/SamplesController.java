package com.soundhive.gui.controllers;

import com.jfoenix.controls.*;
import com.soundhive.core.GenericDeclarations;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class SamplesController extends Controller {

    @FXML
    private JFXListView<HBox> lvSamples;

    @FXML
    private JFXTextField tfTitle;

    @FXML
    private JFXTextArea taDescription;

    @FXML JFXComboBox<GenericDeclarations.License> cbLicense;

    @FXML JFXComboBox<GenericDeclarations.Visibility> cbVisibility;

    @FXML JFXButton btFile;

    @FXML JFXButton btUpload;



    @FXML
    private void initialize() {

    }

    @Override
    protected void start() {

    }
}
