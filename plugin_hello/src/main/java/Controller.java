
import com.jfoenix.controls.JFXComboBox;
import com.soundhive.core.response.Response;
import com.soundhive.core.stats.Keyframe;
import com.soundhive.core.stats.Stats;
import com.soundhive.gui.plugin.PluginController;
import com.soundhive.gui.stats.StatsService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;

import java.util.stream.Collectors;

import static com.soundhive.gui.stats.StatsUtils.generateListenSeries;

public class Controller extends PluginController {
    private StatsService statsService;

    @FXML
    AreaChart<String, Number> acStats;

    @FXML private NumberAxis yAxis;

    @FXML private CategoryAxis xAxis;



    @FXML
    JFXComboBox<StatsService.SpanOption> cbSpan;

    @FXML
    public void initialize() {
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        cbSpan.setOnAction(event -> {
            setStatsService();
            statsService.start();
        });

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
        statsService = new StatsService(getContext().getSession(), this.cbSpan.valueProperty());
        statsService.setOnSucceeded(e -> {
            Response<?> stats = (Response<?>) e.getSource().getValue();
            switch (stats.getStatus()) {
                case SUCCESS:

                    Stats formattedStats = (Stats) stats.getContent();
                    XYChart.Series<String, Number> series = generateListenSeries(formattedStats.getKeyframes());
                    //xAxis.setCategories(FXCollections.observableList(formattedStats.getKeyframes().stream().map(Keyframe::getPeriod).collect(Collectors.toList()));
                    acStats.getData().setAll(series);

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
            getContext().log("Stats request : " + e.getSource().getException().getMessage());
            statsService.reset();
        });
    }

    @Override
    public String getViewName() {
        return "View.fxml";
    }

    @Override
    public String getName() {
        return "StatsPlugin";
    }
}
