module CLI {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires CORE;
    requires java.desktop;
    requires minio;

    opens com.soundhive.gui to javafx.fxml;
    exports com.soundhive.gui;
    exports com.soundhive.gui.controllers;
    exports com.soundhive.gui.plugin;
}