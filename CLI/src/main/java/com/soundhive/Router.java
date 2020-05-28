package com.soundhive;

import com.jfoenix.controls.JFXSnackbar;
import com.soundhive.controllers.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.util.function.Consumer;

public class Router {

    private final AnchorPane anchorPane;


    public Router(final AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }


    public void goTo(final String viewName, final Consumer<Controller> controllerConsumer) {
        final var viewPath = String.format("/com/soundhive/%sView.fxml", viewName);
        final var fxmlLoader = new FXMLLoader(this.getClass().getResource(viewPath));
        goTo(fxmlLoader, controllerConsumer);
    }

    public void goTo(FXMLLoader viewLoader, final Consumer<Controller> controllerConsumer) {
        final var view = loadView(viewLoader, controllerConsumer);

        anchorPane.getChildren().setAll(view);
    }

    private Parent loadView(final FXMLLoader fxmlLoader, final Consumer<Controller> controllerConsumer) {


        try {

            final Parent view = fxmlLoader.load();
            controllerConsumer.accept(fxmlLoader.getController());
            return view;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to load view : ", e);
        }
    }


    public void issueMessage(String message){
        JFXSnackbar bar = new JFXSnackbar(this.anchorPane);
        bar.enqueue(new JFXSnackbar.SnackbarEvent(new Label(message)));
    }

}