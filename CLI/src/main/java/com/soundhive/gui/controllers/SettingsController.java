package com.soundhive.gui.controllers;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.soundhive.gui.plugin.PluginUIContainer;
import com.soundhive.gui.settings.PluginListItemController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class SettingsController extends Controller {
    @FXML
    JFXListView<Pane> lvPlugins;

    @FXML
    JFXButton btLoadPlugin;

    @FXML
    private void  initialize(){
        this.btLoadPlugin.setOnAction(a ->{
            File source = getContext().getRouter().issueFileDialog();
            try {
                getContext().getPluginHandler().HotLoadPlugin(source);
            } catch (IOException e) {
                getContext().getRouter().issueDialog("Could not load plugin file.");
                getContext().logException(e);
            }
             catch (IllegalAccessException
                     | InstantiationException
                     | NoSuchMethodException
                     | ClassNotFoundException
                     | InvocationTargetException e) {
                 getContext().getRouter().issueDialog("Something went wrong with the ");
                 getContext().logException(e);
             }
        });
    }

    @Override
    protected void start() {
        populatePlugins();
    }




    private void populatePlugins() {
        this.lvPlugins.getItems().clear();
        for (PluginUIContainer container : getContext().getPluginHandler().getPlugins()) {
            final var fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/soundhive/gui/templates/PluginListView.fxml"));
            try {
                // load view
                final AnchorPane view = fxmlLoader.load();

                //acquire controller instance and set its plugin
                PluginListItemController controller =  fxmlLoader.getController();
                controller.setPluginAndVerboseAndStart(container, getContext().Verbose());
                controller.setDeleteEvent(plugin -> {
                    getContext().getPluginHandler().deletePlugin(plugin);
                    populatePlugins();
                });

                //ad to list view
                lvPlugins.getItems().add(view);

            } catch (IOException e) {
                getContext().logException(e);
                getContext().getRouter().issueDialog("Error in plugin : " + container.getPlugin().getName());
            }
        }
    }
}
