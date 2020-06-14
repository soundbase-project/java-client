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

    boolean verbose;

    @FXML
    private void initialize() {

    }

    public void setPluginAndVerboseAndStart(PluginUIContainer plugin, boolean verbose) {
        this.plugin = plugin;
        this.verbose = verbose;
        start();
    }

    public void setDeleteEvent(Consumer<PluginUIContainer> pluginConsumer){
        this.pluginConsumer = pluginConsumer;
    }

    private void start() { // TODO :
        try {
            this.lbPluginName.setText(this.plugin.getPlugin().getName());
        } catch (AbstractMethodError e) {
            if (verbose) {
                e.printStackTrace();
            }
            this.lbPluginName.setText("Error");
        }


        this.btDelete.setOnAction(e -> {
            this.pluginConsumer.accept(this.plugin);
        });

        if (!this.plugin.isValid()) {
            lbPluginName.setAccessibleText("This plugin is invalid. Please delete it.");
            this.lbPluginName.setTextFill(Paint.valueOf("red"));
        }
    }
}
