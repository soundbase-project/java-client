package com.soundhive.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import com.soundhive.Router;
import com.soundhive.authentication.SessionHandler;
import com.soundhive.controllers.plugin.IPluginUiController;
import com.soundhive.controllers.plugin.PluginUIContainer;
import com.soundhive.controllers.plugin.PluginUiHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class MainController {
    //create service var

    private Router router;
    private SessionHandler session;

    @FXML
    private JFXListView<JFXButton> lvPluginNavBar;

    @FXML
    private StackPane mainContainer;


    @FXML private Label lbSession;

    @FXML private ImageView ivSession;

    @FXML private AnchorPane appContent;

    @FXML private VBox vbNavMenu;

    @FXML public void initialize() {
        this.router = new Router(appContent);
        this.session = new SessionHandler(lbSession.textProperty());
        loadUIPlugins();

        router.goTo("Login", controller -> controller.setContext(router, session));
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
            router.goTo(target, c -> c.setContext(router, session));
        }
        else {
            JFXDialog dialog = new JFXDialog();
            dialog.setContent(new Label("You have to be connected."));
            dialog.show(mainContainer);
            JFXSnackbar bar = new JFXSnackbar(mainContainer);
            bar.enqueue(new JFXSnackbar.SnackbarEvent(new Label("fuck this shit")));
        }
    }

    private void loadUIPlugins() {
        PluginUiHandler handler = new PluginUiHandler();

        try {
            List<PluginUIContainer> plugins = handler.loadPlugins();
            for (PluginUIContainer plugin :
                    plugins) {
                JFXButton button = new JFXButton();
                button.setText(plugin.getName());
                button.setOnAction(e -> {
                    System.out.println("button clicked.");
                    router.goTo(plugin, c -> c.setContext(router, session));
                });
                this.lvPluginNavBar.getItems().add(button);
            }
        } catch (Exception e){
            e.printStackTrace();
            JFXSnackbar bar = new JFXSnackbar(mainContainer);
            bar.enqueue(new JFXSnackbar.SnackbarEvent(new Label("Unable to load plugins")));

        }

    }

}
