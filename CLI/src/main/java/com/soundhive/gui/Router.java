package com.soundhive.gui;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSnackbar;
import com.soundhive.gui.controllers.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;


import java.io.IOException;
import java.util.function.Consumer;

public class Router {

    private final AnchorPane appContent;
    private final StackPane dialogContainer;


    public Router(final AnchorPane appContent, final StackPane dialogContainer) {
        this.appContent = appContent;
        this.dialogContainer =dialogContainer;
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
            this.issueDialog("Could not load requested view, an error occured.");
            throw new IllegalStateException("Unable to load view : ", e);
        }
    }


    public void issueMessage(String message){
        JFXSnackbar bar = new JFXSnackbar(this.dialogContainer);
        bar.enqueue(new JFXSnackbar.SnackbarEvent(new Label(message)));
    }

    public void issueDialog(String message){
        JFXDialog dialog = new JFXDialog();
        dialog.setContent(new Label(message));
        dialog.show(this.dialogContainer);
    }

}