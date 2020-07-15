package com.soundhive.gui.tracks;

import com.jfoenix.controls.JFXListView;
import com.soundhive.core.tracks.Album;
import com.soundhive.core.tracks.Track;
import com.soundhive.gui.ImageFetchingHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

import java.util.function.Consumer;



public class AlbumListItemController {
    private Album album;
    private Consumer<String> messageLogger;
    private Consumer<Throwable> errorLogger;
    private Consumer<String> updateStats;
    private ImageFetchingHandler imageHandler;


    @FXML
    private Label lbTitle;

    @FXML
    private Label lbListens;

    @FXML
    private ImageView ivArt;

    @FXML
    private JFXListView<AnchorPane> lvTracks;

    @FXML
    private AnchorPane albumPane;


    @FXML
    private void initialize(){
    }


    public void prepareAndStart(final Album album,
                                final ImageFetchingHandler imageHandler,
                                final Consumer<String> updateStats,
                                final Consumer<String> messageLogger,
                                final Consumer<Throwable> errorLogger) {
        this.album = album;
        this.messageLogger = messageLogger;
        this.errorLogger = errorLogger;
        this.updateStats = updateStats;
        this.imageHandler = imageHandler;

        start();
    }

    private void start() {

        this.lbListens.setText("69");

        this.lbTitle.setText(this.album.getTitle());

        fillTracks();
    }

    private void fillTracks() {
        this.lvTracks.getItems().clear();

        if (this.album.getTracks().size() == 1) {
            this.lvTracks.setPrefHeight(0);

        } else if (this.album.getTracks().size() > 1){

            for (Track track :
                    album.getTracks()) {

                Label lbName = new Label(track.getTitle());
                lbName.setTextFill(Paint.valueOf("white"));

                AnchorPane pane = new AnchorPane(lbName);

                pane.setOnMouseClicked( event -> updateStats.accept(track.getID()));
                lvTracks.getItems().add(pane);
            }

            this.albumPane.setPrefHeight(lbTitle.getPrefHeight() + (album.getTracks().size() * 50 + 2) + 20);

            lvTracks.setPrefHeight(album.getTracks().size() * 50 + 2);
        }
        try {
            ivArt.setImage(imageHandler.getImage(album.getCoverFile()));
        }
        catch (IllegalArgumentException e) {

            messageLogger.accept("Could not connect to the file managing server.");

            errorLogger.accept(e);
        }


    }

}
