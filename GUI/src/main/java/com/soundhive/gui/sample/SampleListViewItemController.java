package com.soundhive.gui.sample;

import com.soundhive.core.samples.Sample;
import com.soundhive.gui.Context;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SampleListViewItemController {

    @FXML
    Label lbTitle;

    @FXML Label lbDescription;

    @FXML
    private void initialize() {

    }

    public void setContextAndSample(Context context, Sample sample) {
        this.lbTitle.setText(sample.getTitle());

        this.lbDescription.setText(sample.getDescription());

    }
}
