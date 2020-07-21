package com.soundhive.gui.controllers;

import com.jfoenix.controls.*;
import com.soundhive.core.Enums;
import com.soundhive.core.response.Response;
import com.soundhive.core.samples.Sample;
import com.soundhive.core.tracks.Album;
import com.soundhive.core.upload.SampleUpload;
import com.soundhive.gui.sample.SampleListViewItemController;
import com.soundhive.gui.sample.SampleService;
import com.soundhive.gui.sample.SamplesUploadService;
import com.soundhive.gui.tracks.AlbumListItemController;
import com.soundhive.gui.tracks.TracksService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SamplesController extends Controller {

    @FXML
    public JFXListView<AnchorPane> lvSamples;

    @FXML
    public JFXTextField tfTitle;

    @FXML
    public JFXTextArea taDescription;

    @FXML
    public JFXComboBox<Enums.License> cbLicense;

    @FXML
    public JFXComboBox<Enums.Visibility> cbVisibility;

    @FXML
    public JFXButton btFile;
    public File sampleFile;

    @FXML
    public JFXButton btUpload;

    private SampleService sampleService;

    private SamplesUploadService uploadService;



    @FXML
    private void initialize() {
        cbLicense.getItems().setAll(Enums.License.values());
        cbVisibility.getItems().setAll(Enums.Visibility.values());

        btUpload.setOnAction(e -> {
            setSampleUploadService();
            uploadService.start();

        });

        btFile.setOnAction(e -> {
            this.sampleFile = getContext().getRouter().issueFileDialog("Audio file(.wav, .mp3, .flac)", "*.wav", "*.mp3", "*.flac");
        });
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
            getContext().log("Samples request : " + wrappedSamples.getMessage());
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


    private void setSampleUploadService() {

        this.uploadService = new SamplesUploadService(
                getContext().getSession(),
                tfTitle.textProperty(),
                taDescription.textProperty(),
                cbVisibility.getValue(),
                cbLicense.getValue(),
                this.sampleFile );

        this.uploadService.setOnSucceeded(e -> {
            Response<?> wrappedSamples = (Response<?>) e.getSource().getValue();



            switch (wrappedSamples.getStatus()) {
                case SUCCESS:
                    getContext().getRouter().issueDialog("Sample successfully uploaded.");
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
                    if (wrappedSamples.getException() != null){
                        getContext().logException(wrappedSamples.getException());
                    }

                    break;
            }
            getContext().log("Samples post : " + wrappedSamples.getMessage());
            uploadService.reset();
        });
        this.uploadService.setOnFailed(e -> {
            getContext().logException(e.getSource().getException());
            uploadService.reset();
        });
    }
}
