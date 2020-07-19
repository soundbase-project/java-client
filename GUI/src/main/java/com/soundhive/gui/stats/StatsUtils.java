package com.soundhive.gui.stats;

import com.soundhive.core.stats.Keyframe;
import javafx.scene.chart.XYChart;

import java.text.SimpleDateFormat;
import java.util.List;

public class StatsUtils {
    public static XYChart.Series<String, Number> generateListenSeries(List<Keyframe> keyframes) {
        XYChart.Series<String, Number> listenSeries = new XYChart.Series<>();
        listenSeries.setName("Listens");

        for (Keyframe frame :
                keyframes) {
            listenSeries.getData().add(new XYChart.Data<>(new SimpleDateFormat("dd/MM/yyyy \n hh:mm aa").format(frame.getPeriod()), frame.getPlays()));
        }

        return listenSeries;
    }
}
