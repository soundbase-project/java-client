package com.soundhive.gui.sample;

import com.jfoenix.controls.JFXButton;
import com.soundhive.core.samples.Sample;
import com.soundhive.gui.Context;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SampleListViewItemController {

    private Context context;
    @FXML private Label lbTitle;

    @FXML private Label lbDescription;

    @FXML private JFXButton btDelete;

    @FXML
    private void initialize() {
        btDelete.setOnAction( e -> {

        });
    }

    public void setContextAndSample(Context context, Sample sample) {
        this.lbTitle.setText( sample.getTitle());

        this.lbDescription.setText(sample.getDescription());

        this.context = context;

    }


}
