
package com.soundhive.stats;
import com.soundhive.response.Response;
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
