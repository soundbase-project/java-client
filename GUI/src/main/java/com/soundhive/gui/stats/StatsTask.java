
package com.soundhive.gui.stats;
import com.soundhive.core.response.Response;
import com.soundhive.core.stats.Stats;
import com.soundhive.core.stats.StatsHandler;
import javafx.concurrent.Task;



public class StatsTask extends Task<Response<Stats>> {

    private final StatsHandler stats;

    public StatsTask(final StatsHandler stats) {
        this.stats = stats;

    }

    protected Response<Stats> call() {
        return stats.queryStats();
    }
}
