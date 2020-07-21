package com.soundhive.gui.tracks;

import com.jfoenix.controls.JFXListView;
import com.soundhive.core.tracks.Album;
import com.soundhive.core.tracks.Track;
import com.soundhive.gui.Context;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;



public class AlbumListItemController {
    private Album album;

    private Consumer<String> updateStats;

    private Context context;



    @FXML
    public Label lbTitle;



    @FXML
    public ImageView ivArt;

    @FXML
    public JFXListView<AnchorPane> lvTracks;

    @FXML
    public AnchorPane albumPane;


    @FXML
    public void initialize() {
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


        this.lbTitle.setText(this.album.getTitle());

        populateTracks();
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

    private void populateTracks() {
        this.lvTracks.getItems().clear();
        for (Track track :
                album.getTracks()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/soundhive/gui/templates/TrackListViewItem.fxml"));
            try {
                AnchorPane pane  = loader.load();
                TrackListItemController controller = loader.getController();
                controller.setTrack(track);

                this.albumPane.setPrefHeight(lbTitle.getPrefHeight() + (album.getTracks().size() * 50 + 2) + 20);

                lvTracks.setPrefHeight(album.getTracks().size() * 50 + 2);

                pane.setOnMouseClicked(event -> updateStats.accept(track.getID()));

                this.lvTracks.getItems().add(pane);

            } catch (IOException e) {
                context.logException(e);
                context.getRouter().issueDialog("Error displaying : " + album.getTitle());
            }
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
