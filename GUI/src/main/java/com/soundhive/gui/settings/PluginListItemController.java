package com.soundhive.gui.settings;

import com.jfoenix.controls.JFXButton;
import com.soundhive.gui.plugin.PluginUIContainer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.util.function.Consumer;

public class PluginListItemController {
    private PluginUIContainer plugin;

    @FXML
    private Label lbPluginName;

    @FXML
    private JFXButton btDelete;

    private Consumer<PluginUIContainer> pluginConsumer;

    private Consumer<String> messageLogger;

    private Consumer<Throwable> exceptionLogger;

    @FXML
    private void initialize() {

    }

    public void setPluginAndLoggerAndStart(PluginUIContainer plugin, Consumer<String> messageLogger, Consumer<Throwable> exceptionLogger) {
        this.plugin = plugin;
        this.messageLogger = messageLogger;
        this.exceptionLogger = exceptionLogger;
        start();
    }

    public void setDeleteEvent(Consumer<PluginUIContainer> pluginConsumer) {
        this.pluginConsumer = pluginConsumer;
    }

    private void start() {
        try {
            this.lbPluginName.setText(this.plugin.getPlugin().getName());
        } catch (AbstractMethodError e) {
            messageLogger.accept("Could not obtain name of a plugin");
            exceptionLogger.accept(e);
            this.lbPluginName.setText("Error");
        }


        this.btDelete.setOnAction(e -> this.pluginConsumer.accept(this.plugin));

        if (!this.plugin.isValid()) {
            this.lbPluginName.setTextFill(Paint.valueOf("red"));
        }
    }
}
