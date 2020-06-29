package com.soundhive.gui.upload;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.soundhive.core.upload.InvalidUploadException;
import com.soundhive.core.upload.TrackUpload;
import com.soundhive.gui.Router;
import javafx.fxml.FXML;

import java.io.File;

public class TrackUploadController {



    @FXML private JFXTextField tfTitle;
    @FXML private JFXTextField tfGenre;
    @FXML private JFXTextArea  taDescription;
    @FXML private JFXButton btFile;

    private File trackFile;



    @FXML private void initialize() {

    }

    public void setOpenFileDialog(Router router){
        btFile.setOnAction(e -> {
            this.trackFile = router.issueFileDialog();
        });
    }

    public TrackUpload getTrack() throws InvalidUploadException { //TODO : move checks to object
        if (tfTitle.getText().isEmpty()){
            throw new InvalidUploadException("No title provided for a track.");
        }

        if (tfGenre.getText().isEmpty()){
            throw new InvalidUploadException("No genre provided for the track : " + tfTitle.getText());
        }

        if (tfGenre.getText().isEmpty()){
            throw new InvalidUploadException("No description provided for the track : " + tfTitle.getText());
        }

        if (trackFile == null){
            throw new InvalidUploadException("");
        }
        return new TrackUpload(tfTitle.getText(), tfGenre.getText(), taDescription.getText(), trackFile);
    }
}
