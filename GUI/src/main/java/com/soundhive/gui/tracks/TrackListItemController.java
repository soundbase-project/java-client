package com.soundhive.gui.tracks;

import com.soundhive.core.tracks.Track;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class TrackListItemController {
    private Track track;

    @FXML
    private Label lbTitle;

    @FXML
    private  Label lbListens;


    public void setTrack(final Track track) {
        this.track = track;

        this.lbTitle.setText(track.getTitle());

        this.lbListens.setText(String.valueOf(track.getListens()));
    }
}
