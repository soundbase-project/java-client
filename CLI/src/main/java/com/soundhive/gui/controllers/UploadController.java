package com.soundhive.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.soundhive.core.response.Response;
import com.soundhive.core.upload.InvalidUploadException;
import com.soundhive.core.upload.TrackUpload;
import com.soundhive.gui.upload.TrackUploadController;
import com.soundhive.gui.upload.UploadService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadController extends Controller {


    @FXML private JFXTextField tfTitle;
    @FXML private JFXListView<AnchorPane> lvTracks;
    @FXML private List<TrackUploadController>  trackControllers;
    @FXML private JFXButton btAddTrack;
    @FXML private JFXButton btRemoveTrack;
    @FXML private JFXButton btCoverFile;
    @FXML private JFXButton btUpload;
    @FXML private JFXTextArea taDescription;
    @FXML private ImageView ivCover;

    private UploadService uploadService;
    private File coverFile;
    @FXML
    private void initialize() { // TODO use special new component
        this.trackControllers = new ArrayList<>();
    }

    private void setAlbumUploadService() {
        // TODO : cant use map because of exception. figure out whay
        try {
            List<TrackUpload> tracks = new ArrayList<>();
            for (TrackUploadController controller :
                    this.trackControllers) {
                tracks.add(controller.getTrack());
            }
            this.uploadService = new UploadService(getContext().getSession(), tfTitle.textProperty(), taDescription.textProperty(), this.coverFile, tracks );

            uploadService.setOnSucceeded(e -> {
                Response<?> stats = (Response<?>) e.getSource().getValue();
                switch (stats.getStatus()) {
                    case SUCCESS:
                        getContext().getRouter().issueDialog("Upload succeeded!");
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
                getContext().log("Upload post request : " + stats.getMessage());
            });
            uploadService.setOnFailed(e -> {
                getContext().logException(e.getSource().getException());
                getContext().log(e.getSource().getException().getMessage());
            });

        } catch (InvalidUploadException e) {
            getContext().getRouter().issueDialog("Cannot upload album : " + e.getMessage());
            getContext().log(e.getMessage());
            getContext().logException(e);
        }
    }

    @Override
    protected void start() {
        this.btAddTrack.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/soundhive/gui/templates/TrackUploadListItem.fxml"));
            try {
                AnchorPane view = loader.load();
                TrackUploadController controller = loader.getController();
                controller.setOpenFileDialog(getContext().getRouter());
                this.trackControllers.add(controller);
                this.lvTracks.getItems().add(view);
            } catch (IOException e) {
                getContext().log("Could not add track form.");
                getContext().logException(e);
            }

        });

        btRemoveTrack.setOnAction(event -> {
            if (this.trackControllers.size() > 0) {
                this.lvTracks.getItems().remove(this.lvTracks.getItems().size() - 1);
                this.trackControllers.remove(this.trackControllers.size() - 1);
            }

        });

        btCoverFile.setOnAction(event -> {
            this.coverFile = getContext().getRouter().issueFileDialog("Picture (.png, .jpg)", "*.png", "*.jpg");
            Image cover = new Image("file://" + coverFile.getAbsolutePath());
            this.ivCover.setImage(cover);
        });

        btUpload.setOnAction(event -> {

            setAlbumUploadService();
            uploadService.start();
        } );
    }


}
