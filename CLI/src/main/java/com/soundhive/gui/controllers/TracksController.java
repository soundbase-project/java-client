package com.soundhive.gui.controllers;

import com.soundhive.core.response.Response;
import com.soundhive.core.stats.Stats;
import com.soundhive.core.tracks.Track;
import com.soundhive.gui.tracks.TrackListItemController;
import com.soundhive.gui.tracks.TracksService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;


public class TracksController extends Controller{
    @FXML
    ListView<AnchorPane> lvTracks;

    @FXML
    ListView<AnchorPane> lvAlbums;

    TracksService tracksService;

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
            Response<List<Track>> tracks = (Response<List<Track>>) e.getSource().getValue();



            switch (tracks.getStatus()) {
                case SUCCESS:
                    this.populateTracks(tracks.getContent());
                    break;

                case UNAUTHENTICATED:
                    getContext().getRouter().issueDialog("You were disconnected from your session. Please log in again.");
                    getContext().getRouter().goTo("Login", c -> c.setContextAndStart(getContext()));
                    break;

                case CONNECTION_FAILED:
                    getContext().getRouter().issueDialog("The server is unreachable. Please check your internet connexion.");
                    break;

                case UNKNOWN_ERROR:
                    getContext().getRouter().issueDialog("An error occurred.");
                    break;
            }
            getContext().log("Tracks request : " + tracks.getMessage());
            tracksService.reset();
        });
        this.tracksService.setOnFailed(e -> {
            getContext().logException(e.getSource().getException());
            tracksService.reset();
        });
    }

    private void populateTracks(List<Track> tracks) {
        for (Track track :
                tracks) {
            try {
                FXMLLoader loader = FXMLLoader.load(getClass().getResource("com/soundhive/gui/templates/TrackListViewItem.fxml"));
                TrackListItemController controller = loader.getController();
                this.lvTracks.getItems().add(loader.load());

            } catch (IOException e) {
                getContext().logException(e);
                getContext().getRouter().issueDialog("Error in track : " + track.getTitle());
            }


        }
    }
}
