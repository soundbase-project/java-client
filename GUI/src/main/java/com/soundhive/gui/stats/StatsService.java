package com.soundhive.gui.stats;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import com.soundhive.core.stats.Stats;
import com.soundhive.core.stats.StatsHandler;
import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Service;


public class StatsService extends Service<Response<Stats>> {

    public enum SpanOption {
        LAST_YEAR("Last year"),
        LAST_6_MONTHS("Last 6 months"),
        LAST_TWO_MONTHS("Last two months"),
        LAST_48_HOURS("Last 48 hours"),
        LAST_WEEK("Last week"),
        LAST_DAY("Last day");

        final private String label;

        SpanOption(String label) {
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }

    private final SessionHandler session;

    private final ObjectProperty<SpanOption> cbSpan;

    private final String target;



    StatsHandler.Scope scope;

    /**
     * Get stats for a user
     * @param session Instance of the current session
     * @param cbSpan Timespan for the requested stats
     */
    public StatsService(final SessionHandler session, final ObjectProperty<SpanOption> cbSpan) {
        this.cbSpan = cbSpan;
        this.session = session;
        this.scope = StatsHandler.Scope.USER;
        this.target = session.getUsername();
    }

    /**
     * Get stats per track
     * @param session Instance of the current session
     * @param cbSpan Timespan for the requested stats
     * @param track UUID of the track from which we want the stats
     */
    public StatsService(final SessionHandler session, final ObjectProperty<SpanOption> cbSpan, String track) {
        this.cbSpan = cbSpan;
        this.session = session;
        this.scope = StatsHandler.Scope.TRACKS;
        this.target = track;
    }

    protected StatsTask createTask() {
        SpanOption chosen = cbSpan.getValue();
        StatsHandler stats = null;
        switch (chosen) {
            case LAST_YEAR:
                stats = new StatsHandler(this.session, StatsHandler.Timespan.MONTH, 12, scope, this.target);
                break;
            case LAST_6_MONTHS:
                stats = new StatsHandler(this.session, StatsHandler.Timespan.MONTH, 6,scope, this.target);
                break;
            case LAST_TWO_MONTHS:
                stats = new StatsHandler(this.session, StatsHandler.Timespan.WEEK, 9, scope, this.target);
                break;
            case LAST_WEEK:
                stats = new StatsHandler(this.session, StatsHandler.Timespan.DAY, 7, scope, this.target);
                break;
            case LAST_48_HOURS:
                stats = new StatsHandler(this.session, StatsHandler.Timespan.HOUR, 48, scope, this.target);
                break;
            case LAST_DAY:
                stats = new StatsHandler(this.session, StatsHandler.Timespan.HOUR, 24, scope, this.target);
                break;
        }

        return new StatsTask(stats);
    }
}
