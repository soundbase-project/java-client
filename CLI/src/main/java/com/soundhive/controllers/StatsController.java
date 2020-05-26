package com.soundhive.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.soundhive.Router;
import com.soundhive.authentication.SessionHandler;
import com.soundhive.stats.Keyframe;
import com.soundhive.stats.Stats;
import com.soundhive.stats.StatsHandler;
import com.soundhive.stats.StatsService;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

public class StatsController implements IUiController {
    private Router router;
    private SessionHandler session;
    private StatsService statsService;

    @FXML
    AreaChart<String, Number> acStats;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    JFXComboBox<StatsService.SpanOption> cbSpan;

    @FXML
    public void initialize() {
        populateSpans();
        populateChart();
    }


    @Override
    public void setContext(Router router, SessionHandler session) {
        this.router = router;
        this.session = session;
    }

    private void populateSpans() {
        cbSpan.getItems().setAll(StatsService.SpanOption.values());
        cbSpan.setValue(StatsService.SpanOption.LAST_WEEK);

    }

    private void populateChart() {
        statsService = new StatsService(this.session, this.cbSpan.valueProperty(), StatsHandler.Scope.USER);
        this.acStats.setTitle("Views from last week");
        statsService.setOnSucceeded(e -> {
            Stats stats = (Stats) e.getSource().getValue();
            this.acStats.getData().add(generateListenSeries(stats.getKeyframes()));
        });
        statsService.setOnFailed(e -> {
            e.getSource().getException().printStackTrace();

        });
        statsService.start();

    }

    XYChart.Series<String, Number> generateListenSeries(List<Keyframe> keyframes){
        XYChart.Series<String, Number> listenSeries = new XYChart.Series<>();
        listenSeries.setName("Listens");

        for (Keyframe frame :
                keyframes) {
            listenSeries.getData().add(new XYChart.Data<>(frame.getPeriod(), frame.getPlays()));
        }

        return listenSeries;
    }
}
