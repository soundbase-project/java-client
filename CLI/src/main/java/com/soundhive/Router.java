package com.soundhive;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.util.function.Consumer;

public class Router {

    private final AnchorPane anchorPane;



    public Router(final AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }



    public <T> void goTo(final String viewName, final Consumer<T> controllerConsumer) {
        final var view = loadView(viewName, controllerConsumer);

        anchorPane.getChildren().setAll(view);
    }

    private <T> Parent loadView(final String viewName, final Consumer<T> controllerConsumer) {
        final var viewPath = String.format("/com/soundhive/%sView.fxml", viewName);

        try {
            final var fxmlLoader = new FXMLLoader(this.getClass().getResource(viewPath));
            final Parent view = fxmlLoader.load();
            controllerConsumer.accept(fxmlLoader.getController());

            return view;
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Unable to load view: %s", viewPath), e);
        }
    }
}
