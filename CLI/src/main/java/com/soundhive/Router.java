package com.soundhive;

import com.soundhive.controllers.IUiController;
import com.soundhive.controllers.plugin.IPluginUiController;
import com.soundhive.controllers.plugin.PluginUIContainer;
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


    public void goTo(final String viewName, final Consumer<IUiController> controllerConsumer) {

        final var view = loadView(viewName, controllerConsumer);
        anchorPane.getChildren().setAll(view);
    }

    public void goTo(PluginUIContainer plugin, final Consumer<IUiController> controllerConsumer) {
        final var view = loadView(plugin, controllerConsumer);
        anchorPane.getChildren().setAll(view);
    }

    private Parent loadView(final String viewName, final Consumer<IUiController> controllerConsumer) {
        final var viewPath = String.format("/com/soundhive/%sView.fxml", viewName);

        try {
            final var fxmlLoader = new FXMLLoader(this.getClass().getResource(viewPath));
            final Parent view = fxmlLoader.load();
            controllerConsumer.accept(fxmlLoader.getController());
            return view;
        } catch (IOException e) {
            e.printStackTrace();
            //throw new IllegalStateException(String.format("Unable to load view: %s", viewURL), e);
        }
        return new AnchorPane();
    }

    private Parent loadView(PluginUIContainer plugin, final Consumer<IUiController> controllerConsumer) {
        try {
            final var fxmlLoader = new FXMLLoader(plugin.getView());
            fxmlLoader.setController(plugin.getPlugin());
            controllerConsumer.accept(fxmlLoader.getController());
            final Parent view = fxmlLoader.load();
            return view;
        } catch (IOException e) {
            e.printStackTrace();
            //throw new IllegalStateException(String.format("Unable to load view: %s", viewURL), e);
        }
        return new AnchorPane();
    }


}