package com.soundhive.gui;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSnackbar;
import com.soundhive.gui.controllers.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class Router {

    private final AnchorPane appContent;
    private final StackPane dialogContainer;
    private final Stage stage;

    public Router(final AnchorPane appContent, final StackPane dialogContainer, final Stage stage) {
        this.appContent = appContent;
        this.dialogContainer = dialogContainer;
        this.stage = stage;
    }


    public void goTo(final String viewName, final Consumer<Controller> controllerConsumer) {
        final var viewPath = String.format("/com/soundhive/gui/%sView.fxml", viewName);
        final var fxmlLoader = new FXMLLoader(this.getClass().getResource(viewPath));
        goTo(fxmlLoader, controllerConsumer);
    }

    public void goTo(FXMLLoader viewLoader, final Consumer<Controller> controllerConsumer) {
        final var view = loadView(viewLoader, controllerConsumer);

        appContent.getChildren().setAll(view);
    }

    private Parent loadView(final FXMLLoader fxmlLoader, final Consumer<Controller> controllerConsumer)  {
        try {

            final Parent view = fxmlLoader.load();
            controllerConsumer.accept(fxmlLoader.getController());
            return view;
        } catch (IOException e) {
            e.printStackTrace();
            this.issueDialog("Could not load requested view, an error occurred.");
            throw new IllegalStateException("Unable to load view : ", e);
        }
    }


    public void issueMessage(String message){
        JFXSnackbar bar = new JFXSnackbar(this.dialogContainer);
        Label lab = new Label(message);
        lab.setTextFill(Paint.valueOf("white"));
        bar.enqueue(new JFXSnackbar.SnackbarEvent(lab));
    }

    public void issueDialog(String message){
        JFXDialog dialog = new JFXDialog();
        dialog.setContent(new Label(message));
        dialog.show(this.dialogContainer);
    }

    public File issueFileDialog(String contentDesc,  String ...extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(contentDesc, extensions));
        return fileChooser.showOpenDialog(stage);
    }
}