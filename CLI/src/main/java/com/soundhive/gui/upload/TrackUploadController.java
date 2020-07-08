package com.soundhive.gui.upload;

import com.jfoenix.controls.*;
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
    @FXML private JFXComboBox<TrackUpload.License> cbLicense;
    @FXML private JFXCheckBox cbDownloadable;

    private File trackFile;



    @FXML private void initialize() { // TODO : add license and downloadable support
        cbLicense.getItems().setAll(TrackUpload.License.values());
    }

    public void setOpenFileDialog(Router router){
        btFile.setOnAction(e -> {
            this.trackFile = router.issueFileDialog("Audio files (.wav, .mp3)", "*.wav", "*.mp3");
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
        return new TrackUpload(tfTitle.getText(), tfGenre.getText(), taDescription.getText(), trackFile, cbLicense.getValue(), cbDownloadable.isSelected());
    }
}
