package com.soundhive.gui.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.soundhive.core.response.Response;
import com.soundhive.core.stats.Stats;
import com.soundhive.core.tracks.Album;
import com.soundhive.gui.stats.StatsService;
import com.soundhive.gui.tracks.AlbumListItemController;
import com.soundhive.gui.tracks.TracksService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

import static com.soundhive.gui.stats.StatsUtils.generateListenSeries;


public class TracksController extends Controller{
    @FXML
    private ListView<AnchorPane> lvTracks;

    // @FXML private ListView<AnchorPane> lvAlbums;

    private TracksService tracksService;

    private StatsService statsService;

    @FXML
    private AreaChart<String, Number> acStats;

    @FXML
    private JFXComboBox<StatsService.SpanOption> cbSpan;

    @FXML
    private void initialize() {
        this.populateSpans();
    }

    @Override
    protected void start() {
        setTracksService();
        tracksService.start();

    }

    private void setTracksService() {
        this.tracksService = new TracksService(getContext().getSession());
        this.tracksService.setOnSucceeded(e -> {
            Response<?> Albums = (Response<?>) e.getSource().getValue();



            switch (Albums.getStatus()) {
                case SUCCESS:
                    var albums = (List<?>) Albums.getContent();
                    this.populateTracks(albums);
                    break;

                case UNAUTHENTICATED:
                    getContext().getRouter().issueDialog("You were disconnected from your session. Please log in again.");
                    getContext().getRouter().goTo("Login", c -> c.setContextAndStart(getContext()));
                    getContext().getSession().destroySession();
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

    private void populateTracks(List<?> albums) {
        lvTracks.getItems().clear();
        for (var rawAlbum :
                albums) {
            Album album = (Album) rawAlbum;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/soundhive/gui/templates/AlbumListViewItem.fxml"));
            try {


                AnchorPane pane  = loader.load();
                AlbumListItemController controller = loader.getController();
                controller.prepareAndStart(album,
                        getContext().getPicHandler(),
                        this::updateStats,
                        getContext()::log,
                        getContext()::logException);
                this.lvTracks.getItems().add(pane);

            } catch (IOException e) {
                getContext().logException(e);
                getContext().getRouter().issueDialog("Error displaying : " + album.getTitle());
            }
        }
    }

    private void updateStats(String track_id) {
        this.acStats.getData().clear();
        setStatsService(track_id);
        statsService.start();
    }

    private void setStatsService(String track_id) {
        statsService = new StatsService(getContext().getSession(), cbSpan.valueProperty(), track_id);
        statsService.setOnSucceeded(e -> {
            Response<?> stats = (Response<?>) e.getSource().getValue();
            switch (stats.getStatus()) {
                case SUCCESS:
                    this.acStats.getData().add(generateListenSeries(((Stats)stats.getContent()).getKeyframes()));
                    break;

                case UNAUTHENTICATED:
                    getContext().getRouter().issueDialog("You were disconnected from your session. Please log in again.");
                    getContext().getSession().destroySession();
                    break;

                case CONNECTION_FAILED:
                    getContext().getRouter().issueDialog("The server is unreachable. Please check your internet connexion.");
                    break;

                case INTERNAL_ERROR:
                    getContext().getRouter().issueDialog("An error occurred.");
                    break;
            }
            getContext().log("Stats request : " + stats.getMessage());
            statsService.reset();
        });
        statsService.setOnFailed(e -> {
            getContext().logException(e.getSource().getException());
            statsService.reset();
        });
    }

    private void populateSpans() {
        cbSpan.getItems().setAll(StatsService.SpanOption.values());
        cbSpan.setValue(StatsService.SpanOption.LAST_WEEK);
    }
}
