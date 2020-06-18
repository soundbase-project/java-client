package com.soundhive.gui.controllers;

import com.soundhive.core.generic.Generic;
import com.soundhive.core.response.Response;
import com.soundhive.core.tracks.Album;
import com.soundhive.gui.tracks.AlbumListItemController;
import com.soundhive.gui.tracks.TracksService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;


public class TracksController extends Controller{
    @FXML
    private ListView<AnchorPane> lvTracks;

    @FXML
    private ListView<AnchorPane> lvAlbums;

    private TracksService tracksService;

    @FXML
    private void initialize() {

    }

    @Override
    protected void start() {
        setTracksService();
        this.tracksService.start();
    }

    private void setTracksService() { // TODO generify  query service settings initialisation
        this.tracksService = new TracksService(getContext().getSession());
        this.tracksService.setOnSucceeded(e -> {
            Response<List<Album>> Albums = Generic.secureResponseCast((Response<?>) e.getSource().getValue());



            switch (Albums.getStatus()) {
                case SUCCESS:
                    this.populateTracks(Albums.getContent());
                    break;

                case UNAUTHENTICATED:
                    getContext().getRouter().issueDialog("You were disconnected from your session. Please log in again.");
                    getContext().getRouter().goTo("Login", c -> c.setContextAndStart(getContext()));
                    break;

                case CONNECTION_FAILED:
                    getContext().getRouter().issueDialog("The server is unreachable. Please check your internet connexion.");
                    getContext().log(Albums.getMessage());
                    break;

                case INTERNAL_ERROR:
                    getContext().getRouter().issueDialog("An error occurred.");
                    getContext().log(Albums.getMessage());
                    getContext().logException(Albums.getException());
                    break;
            }
            getContext().log("Tracks request : " + Albums.getMessage());
            tracksService.reset();
        });
        this.tracksService.setOnFailed(e -> {
            getContext().logException(e.getSource().getException());
            tracksService.reset();
        });
    }

    private void populateTracks(List<Album> albums) {
        lvTracks.getItems().clear();
        for (Album album :
                albums) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/soundhive/gui/templates/AlbumListViewItem.fxml"));
            try {


                AnchorPane pane  = loader.load();
                AlbumListItemController controller = loader.getController();
                controller.setAlbumsAndLoggersAndStart(album,
                        getContext()::log,
                        getContext()::logException);
                this.lvTracks.getItems().add(pane);

            } catch (IOException e) {
                getContext().logException(e);
                getContext().getRouter().issueDialog("Error displaying : " + album.getTitle());
            }


        }
    }
}
