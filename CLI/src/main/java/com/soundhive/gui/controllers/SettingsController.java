package com.soundhive.gui.controllers;
// TODO : plugin management system

import com.jfoenix.controls.JFXListView;
import com.soundhive.gui.plugin.PluginUIContainer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SettingsController extends Controller {
    @FXML
    JFXListView<Pane> lvPlugins;

    @FXML
    private void  initialize(){

    }

    @Override
    protected void start() {
        for (PluginUIContainer container : getContext().getPlugins()) {
            final var fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/soundhive/gui/PluginListView.fxml"));
            try {
                final var view = fxmlLoader.load();
                lvPlugins.getItems().add((AnchorPane) view);
            } catch (IOException e) {
                if (getContext().Verbose()) {
                    e.printStackTrace();
                }
                getContext().getRouter().issueDialog("Error displaying plugin : " + container.getName());
                throw new IllegalStateException("Unable to load view : ", e);
            }
        }
    }
}
