package com.soundhive.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import com.soundhive.gui.Router;
import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.gui.controllers.plugin.PluginUIContainer;
import com.soundhive.gui.controllers.plugin.PluginUiHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public class MainController {
    //create service var

    private Router router;
    private SessionHandler session;

    @FXML
    private JFXListView<HBox> lvPluginNavBar;

    @FXML
    private StackPane mainContainer;


    @FXML private Label lbSession;

    @FXML private ImageView ivSession;

    @FXML private AnchorPane appContent;

    @FXML private VBox vbNavMenu;

    @FXML public void initialize() {
        this.router = new Router(appContent, mainContainer);
        this.session = new SessionHandler();
        loadUIPlugins();

        router.goTo("Login", controller -> controller.setContextAndStart(router, session));
    }

    public void setRouter(final Router router) {
        this.router = router;
    }

    @FXML
    private void navToStats() {
        tryGoingTo("Stats");
    }

    @FXML
    private void navToTracks() {
        tryGoingTo("Tracks");
    }

    @FXML
    private void navToSamples() {
        tryGoingTo("Samples");
    }

    @FXML
    private void navToUpload() {
        tryGoingTo("Upload");
    }

    @FXML
    private void navToSettings() {
        tryGoingTo("Settings");
    }

    private void tryGoingTo(String target) {
        if (session.isConnected()) {
            router.goTo(target, c -> c.setContextAndStart(router, session));
        }
        else {
            router.issueMessage("You have to be connected.");

        }
    }

    private void loadUIPlugins() {
        PluginUiHandler handler = new PluginUiHandler();

        try {
            List<PluginUIContainer> plugins = handler.loadPlugins();
            for (PluginUIContainer plugin :
                    plugins) {

                JFXButton button = new JFXButton();
                setButtonStyle(button);
                button.setText(plugin.getName());
                button.setOnAction(e -> {
                    router.goTo(plugin.getView(), c -> c.setContextAndStart(router, session));
                });
                HBox buttonPane = new HBox(button);
                buttonPane.setStyle("-fx-background-color: #343a40");

                this.lvPluginNavBar.getItems().add(buttonPane);
                this.lvPluginNavBar.setStyle("-fx-background-color: #343a40");
            }
        } catch (Exception e){
            e.printStackTrace();
            this.router.issueMessage("Unable to load plugins");
        }
    }


    private void setButtonStyle(JFXButton button) {
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: #343a40");
        button.setMaxHeight(Double.MAX_VALUE);
        button.setMaxWidth(Double.MAX_VALUE);


    }

}
