package com.soundhive.gui.settings;

import com.jfoenix.controls.JFXButton;
import com.soundhive.gui.plugin.PluginUIContainer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.function.Consumer;

public class PluginListViewController {
    private PluginUIContainer plugin;

    @FXML
    private Label lbPluginName;

    @FXML
    private JFXButton btDelete;

    private Consumer<PluginUIContainer> pluginConsumer;


    @FXML
    private void initialize() {

    }

    public void setPluginAndStart(PluginUIContainer plugin) {
        this.plugin = plugin;
        start();
    }

    public void setDeleteEvent(Consumer<PluginUIContainer> pluginConsumer){
        this.pluginConsumer = pluginConsumer;
    }

    private void start() {
        this.lbPluginName.setText(this.plugin.getPlugin().getName());

        this.btDelete.setOnAction(e -> {
            this.pluginConsumer.accept(this.plugin);
        });
    }
}
