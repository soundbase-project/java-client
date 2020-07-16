
package com.soundhive.gui.stats;
import com.soundhive.core.Enums.*;
import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import com.soundhive.core.stats.Stats;
import com.soundhive.core.stats.StatsQueries;
import javafx.concurrent.Task;



public class StatsTask extends Task<Response<Stats>> {

    private final SessionHandler session;

    private final Timespan timespan;

    private final int nb;

    private final Scope scope;

    private final String target;

    public StatsTask(final SessionHandler session, final Timespan timespan, int nb,final  Scope scope, String target) {
        this.session = session;

        this.timespan = timespan;

        this.nb = nb;

        this.target = target;

        this.scope = scope;
    }

    protected Response<Stats> call() {
        return StatsQueries.queryStats(session, timespan, nb, scope,  target);
    }
}
