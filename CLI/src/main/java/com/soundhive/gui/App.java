package com.soundhive.gui;

import com.soundhive.core.conf.ConfHandler;
import com.soundhive.gui.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(final Stage stage) {


        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("MainView.fxml"));
        final Parent view;
        try {
            view = loader.load();
            MainController controller = loader.getController();
            controller.setStage(stage);
            Scene scene = new Scene(view);
            scene.getStylesheets().add(getClass().getResource("/com/soundhive/gui/style/style.css").toString());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stage.setTitle("SoundHive");
        Image icon = new Image(getClass().getResourceAsStream("/drawable/logo-icon.png"));
        stage.getIcons().add(icon);
        stage.show();
    }



    public static void main(final String[] args) {
        launch(args);
    }


}