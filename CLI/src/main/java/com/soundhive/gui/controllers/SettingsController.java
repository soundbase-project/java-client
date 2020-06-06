package com.soundhive.gui.controllers;
// TODO : plugin management system

import com.jfoenix.controls.JFXListView;
import com.soundhive.gui.plugin.PluginUIContainer;
import com.soundhive.gui.settings.PluginListViewController;
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
        populatePlugins();
    }

    private void populatePlugins() {
        this.lvPlugins.getItems().clear();
        for (PluginUIContainer container : getContext().getPlugins()) {
            final var fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/soundhive/gui/templates/PluginListView.fxml"));
            try {
                // load view
                final AnchorPane view = fxmlLoader.load();

                //acquire controller instance and set its plugin
                PluginListViewController controller =  fxmlLoader.getController();
                controller.setPluginAndStart(container);
                controller.setDeleteEvent(plugin -> {
                    getContext().deletePlugin(plugin);
                    populatePlugins();
                });

                //ad to list view
                lvPlugins.getItems().add(view);

            } catch (IOException e) {
                if (getContext().Verbose()) {
                    e.printStackTrace();
                }
                getContext().getRouter().issueDialog("Error in plugin : " + container.getPlugin().getName());
                throw new IllegalStateException("Unable to load view : ", e);
            }
        }
    }
}
