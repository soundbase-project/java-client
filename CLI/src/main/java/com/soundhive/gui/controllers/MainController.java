package com.soundhive.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.soundhive.core.conf.ConfigFileException;
import com.soundhive.core.conf.MissingParamException;
import com.soundhive.gui.Context;
import com.soundhive.gui.Router;
import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.gui.controllers.plugin.PluginUIContainer;
import com.soundhive.gui.controllers.plugin.PluginUiHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class MainController {
    //create service var

    private Context context;

    @FXML
    private JFXListView<HBox> lvPluginNavBar;

    @FXML
    private StackPane mainContainer;


    @FXML private Label lbSession;

    @FXML private ImageView ivSession;

    @FXML private AnchorPane appContent;

    @FXML private VBox vbNavMenu;

    @FXML public void initialize() {
        initContext();

        try {
            loadUIPlugins();
        } catch (MissingParamException e) {
            this.context.getRouter().issueDialog("Impossible to load plugins : \n" + e.getMessage());
            if (context.Verbose()) {
                e.printStackTrace();
            }
        } catch (Exception e) { //TODO : centralize exceptions
            this.context.getRouter().issueDialog("Impossible to load plugin for unknown reasons.");
            if (context.Verbose()) {
                e.printStackTrace();
            }
        }


        context.getRouter().goTo("Login", controller -> controller.setContextAndStart(this.context));
    }

    private void initContext(){
        try {
            this.context = new Context(new Router(this.appContent, this.mainContainer));
        }
        catch (ConfigFileException | MissingParamException e) {
            issueNotWorkingNotice(e.getMessage());
        }
    }

    private void issueNotWorkingNotice(String message) {

        Label secondLabel = new Label(message);
        JFXButton button = new JFXButton("Exit");
        VBox box = new VBox();

        button.setOnAction(e -> {
            System.exit(-1);
        });

        box.getChildren().setAll(secondLabel, button);
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(box);

        Scene secondScene = new Scene(secondaryLayout, 300, 150);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("ERROR");
        newWindow.setScene(secondScene);


        newWindow.show();

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

    private void tryGoingTo(String target) { //TODO : uncomment
        //if (session.isConnected()) {
            context.getRouter().goTo(target, c -> c.setContextAndStart(this.context));
        //}
        //else {
        //    router.issueMessage("You have to be connected.");

        //}
    }



    private void loadUIPlugins() throws Exception{
        String uiPluginDir = context.getConf().getParam("ui_plugin_dir");
        PluginUiHandler handler = new PluginUiHandler(uiPluginDir);



        List<PluginUIContainer> plugins = handler.loadPlugins();
        for (PluginUIContainer plugin :
                plugins) {

            JFXButton button = new JFXButton();
            setButtonStyle(button);
            button.setText(plugin.getName());
            button.setOnAction(e -> {
                context.getRouter().goTo(plugin.getView(), c -> c.setContextAndStart(context));
            });
            HBox buttonPane = new HBox(button);
            buttonPane.setStyle("-fx-background-color: #343a40");

            this.lvPluginNavBar.getItems().add(buttonPane);
            this.lvPluginNavBar.setStyle("-fx-background-color: #343a40");
        }

    }


    private void setButtonStyle(JFXButton button) {
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: #343a40");
        button.setMaxHeight(Double.MAX_VALUE);
        button.setMaxWidth(Double.MAX_VALUE);


    }
}
