package com.soundhive.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.soundhive.core.conf.ConfigFileException;
import com.soundhive.core.conf.MissingParamException;
import com.soundhive.gui.Context;
import com.soundhive.gui.Router;
import com.soundhive.gui.plugin.PluginUIContainer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class MainController {
    //create service var

    private Context context;

    private Stage stageRef;

    @FXML
    private JFXListView<AnchorPane> lvPluginNavBar;

    @FXML
    private StackPane mainContainer;


    @FXML private Label lbSession;

    @FXML private ImageView ivSession;

    @FXML private AnchorPane appContent;

    @FXML private JFXButton btExit;

    @FXML private JFXButton btLogout;

    @FXML public void initialize() {
        initContext();

        displayPlugins(this.context.getPluginHandler().getPlugins());

        context.getRouter().goTo("Login", controller -> controller.setContextAndStart(this.context));

        btExit.setOnAction(e -> System.exit(0));

        btLogout.setOnAction(e -> {
            context.getSession().destroySession();
            ivSession.setImage(null);
            lbSession.setText("disconnected");
            context.getRouter().goTo("Login", controller -> controller.setContextAndStart(this.context));
        });
    }

    private void initContext() {

        try {
            this.context = new Context(new Router(this.appContent, this.mainContainer, this.stageRef), this::setUserInfos, this::displayPlugins);

        }
        catch (ConfigFileException | MissingParamException e) {
            issueNotWorkingNotice(e.getMessage());
            e.printStackTrace();
        }
        catch ( Exception e ) {
            issueNotWorkingNotice("An error occurred while launching the app.");
            e.printStackTrace();
        }

    }

    //pichandler has not been initialized yet at this moment
    private void setUserInfos(String username, Image picture) {
        this.lbSession.setText(username);

        this.ivSession.setImage(picture);
    }

    private void issueNotWorkingNotice(String message) {

        Label secondLabel = new Label(message);
        JFXButton button = new JFXButton("Exit");
        VBox box = new VBox();

        button.setOnAction(e -> System.exit(-1));

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

    private void tryGoingTo(String target) {
        if (context.getSession().isConnected()) {
            context.getRouter().goTo(target, c -> c.setContextAndStart(this.context));
        }
        else {
            context.getRouter().issueMessage("You have to be connected.");

        }
    }

    private  void displayPlugins(List<PluginUIContainer> plugins) {
        lvPluginNavBar.getItems().clear();
        for (PluginUIContainer plugin :
                plugins) {
            if (!plugin.isValid()) {
                continue;
            }

            JFXButton button = new JFXButton();



            setButtonStyle(button);
            button.setText(plugin.getPlugin().getName());
            button.setOnAction(e -> context.getRouter().goTo(plugin.getView(), c -> c.setContextAndStart(context)));
            AnchorPane pane = new AnchorPane(button);
            //buttonPane.setStyle("-fx-background-color: #343a40");

            this.lvPluginNavBar.getItems().add(pane);
            this.lvPluginNavBar.setStyle("-fx-background-color: #343a40");
        }
    }


    public void setStage(Stage stage) {
        this.stageRef = stage;
    }

    private void setButtonStyle(JFXButton button) {
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: #343a40;");

    }
}
