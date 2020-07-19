package com.soundhive.gui.tracks;

import com.jfoenix.controls.JFXListView;
import com.soundhive.core.tracks.Album;
import com.soundhive.core.tracks.Track;
import com.soundhive.gui.Context;
import com.soundhive.gui.ImageFetchingHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

import java.util.function.Consumer;



public class AlbumListItemController {
    private Album album;

    private Consumer<String> updateStats;

    private Context context;



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
    private void initialize() {
    }


    public void prepareAndStart(final Context context,
                                final Album album,
                                final Consumer<String> updateStats) {
        this.album = album;
        this.context = context;

        this.updateStats = updateStats;


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

        } else if (this.album.getTracks().size() > 1) {

            for (Track track :
                    album.getTracks()) {

                Label lbName = new Label(track.getTitle());
                lbName.setTextFill(Paint.valueOf("white"));

                AnchorPane pane = new AnchorPane(lbName);

                pane.setOnMouseClicked(event -> updateStats.accept(track.getID()));
                lvTracks.getItems().add(pane);
            }

            this.albumPane.setPrefHeight(lbTitle.getPrefHeight() + (album.getTracks().size() * 50 + 2) + 20);

            lvTracks.setPrefHeight(album.getTracks().size() * 50 + 2);
        }
        try {
            ivArt.setImage(context.getPicHandler().getImage(album.getCoverFile()));
        }
        catch (IllegalArgumentException e) {

            context.log("Could not connect to the file managing server.");

            context.logException(e);
        }


    }

}
