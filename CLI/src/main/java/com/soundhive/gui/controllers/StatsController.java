package com.soundhive.gui.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.soundhive.core.response.Response;
import com.soundhive.core.stats.Stats;
import com.soundhive.gui.stats.StatsService;
import com.soundhive.core.stats.Keyframe;
import com.soundhive.core.stats.StatsHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
// import javafx.scene.chart.CategoryAxis;
// import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

public class StatsController extends  Controller{
    private StatsService statsService;

    @FXML
    AreaChart<String, Number> acStats;

    // @FXML private NumberAxis yAxis;

    // @FXML private CategoryAxis xAxis;

    @FXML
    JFXComboBox<StatsService.SpanOption> cbSpan;

    @FXML
    public void initialize() {


    }


    @Override
    protected void start() {
        populateSpans();

        setStatsService();
        statsService.start();
    }

    private void populateSpans() {
        cbSpan.getItems().setAll(StatsService.SpanOption.values());
        cbSpan.setValue(StatsService.SpanOption.LAST_WEEK);

    }


    private void setStatsService() {
        statsService = new StatsService(getContext().getSession(), this.cbSpan.valueProperty(), StatsHandler.Scope.USER);
        statsService.setOnSucceeded(e -> {
            Response<?> stats = (Response<?>) e.getSource().getValue();
            switch (stats.getStatus()) {
                case SUCCESS:
                    this.acStats.getData().add(generateListenSeries(((Stats)stats.getContent()).getKeyframes()));
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
            getContext().log("Stats request : " + stats.getMessage());
            statsService.reset();
        });
        statsService.setOnFailed(e -> {
            getContext().logException(e.getSource().getException());
            statsService.reset();
        });
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
