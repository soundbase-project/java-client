package com.soundhive.gui.upload;

import com.jfoenix.controls.*;
import com.soundhive.core.Enums;
import com.soundhive.core.upload.InvalidUploadException;
import com.soundhive.core.upload.TrackUpload;
import com.soundhive.gui.Router;
import javafx.fxml.FXML;

import java.io.File;

public class TrackUploadController {



    @FXML public JFXTextField tfTitle;
    @FXML public JFXTextField tfGenre;
    @FXML public JFXTextArea  taDescription;
    @FXML public JFXButton btFile;
    @FXML public JFXComboBox<Enums.License> cbLicense;
    @FXML public JFXCheckBox cbDownloadable;

    private File trackFile;



    @FXML private void initialize() {
        cbLicense.getItems().setAll(Enums.License.values());
    }

    public void setOpenFileDialog(Router router) {
        btFile.setOnAction(e -> this.trackFile = router.issueFileDialog("Audio files (.wav, .mp3)", "*.wav", "*.mp3"));
    }

    public TrackUpload getTrack() throws InvalidUploadException {
        return new TrackUpload(tfTitle.getText(), tfGenre.getText(), taDescription.getText(), trackFile, cbLicense.getValue(), cbDownloadable.isSelected());
    }
}
