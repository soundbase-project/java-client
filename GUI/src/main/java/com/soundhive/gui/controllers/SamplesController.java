package com.soundhive.gui.controllers;

import com.jfoenix.controls.*;
import com.soundhive.core.Enums;
import com.soundhive.core.response.Response;
import com.soundhive.core.samples.Sample;
import com.soundhive.core.tracks.Album;
import com.soundhive.gui.sample.SampleListViewItemController;
import com.soundhive.gui.sample.SampleService;
import com.soundhive.gui.tracks.AlbumListItemController;
import com.soundhive.gui.tracks.TracksService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;

public class SamplesController extends Controller {

    @FXML
    private JFXListView<AnchorPane> lvSamples;

    @FXML
    private JFXTextField tfTitle;

    @FXML
    private JFXTextArea taDescription;

    @FXML
    private JFXComboBox<Enums.License> cbLicense;

    @FXML
    private JFXComboBox<Enums.Visibility> cbVisibility;

    @FXML
    private JFXButton btFile;

    @FXML
    private JFXButton btUpload;

    private SampleService sampleService;



    @FXML
    private void initialize() {
        cbLicense.getItems().setAll(Enums.License.values());
        cbVisibility.getItems().setAll(Enums.Visibility.values());
    }

    @Override
    protected void start() {

        setSamplesService();

        sampleService.start();
    }


    private void setSamplesService() {
        this.sampleService = new SampleService(getContext().getSession());
        this.sampleService.setOnSucceeded(e -> {
            Response<?> wrappedSamples = (Response<?>) e.getSource().getValue();



            switch (wrappedSamples.getStatus()) {
                case SUCCESS:
                    var samples = (List<?>) wrappedSamples.getContent();
                    this.populateSamples(samples);
                    break;

                case UNAUTHENTICATED:
                    getContext().getRouter().issueDialog("You were disconnected from your session. Please log in again.");
                    getContext().getRouter().goTo("Login", c -> c.setContextAndStart(getContext()));
                    getContext().getSession().destroySession();
                    break;

                case CONNECTION_FAILED:
                    getContext().getRouter().issueDialog("The server is unreachable. Please check your internet connexion.");
                    getContext().log(wrappedSamples.getMessage());
                    break;

                case INTERNAL_ERROR:
                    getContext().getRouter().issueDialog("An error occurred.");
                    getContext().log(wrappedSamples.getMessage());
                    getContext().logException(wrappedSamples.getException());
                    break;
            }
            getContext().log("Tracks request : " + wrappedSamples.getMessage());
            sampleService.reset();
        });
        this.sampleService.setOnFailed(e -> {
            getContext().logException(e.getSource().getException());
            sampleService.reset();
        });
    }

    private void populateSamples(List<?> samples) {
        lvSamples.getItems().clear();
        for (var rawSample :
                samples) {
            Sample sample = (Sample) rawSample;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/soundhive/gui/templates/SampleListViewItem.fxml"));
            try {
                AnchorPane pane  = loader.load();
                SampleListViewItemController controller = loader.getController();
                controller.setContextAndSample(getContext(), sample);
                this.lvSamples.getItems().add(pane);

            } catch (IOException e) {
                getContext().logException(e);
                getContext().getRouter().issueDialog("Error displaying : " + sample.getTitle());
            }
        }
    }
}
