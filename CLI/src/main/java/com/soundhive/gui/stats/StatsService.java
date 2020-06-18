package com.soundhive.gui.stats;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import com.soundhive.core.stats.Stats;
import com.soundhive.core.stats.StatsHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;


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



    StatsHandler.Scope scope;

    public StatsService(final SessionHandler session, final ObjectProperty<SpanOption> cbSpan, final StatsHandler.Scope scope) {
        this.cbSpan = cbSpan;
        this.session = session;
        this.scope = scope;
    }

    protected StatsTask createTask() {
        SpanOption chosen = cbSpan.getValue();
        StatsHandler stats = null;
        switch (chosen) {
            case LAST_YEAR:
                stats = new StatsHandler(this.session, StatsHandler.Timespan.MONTH, 12, scope);
                break;
            case LAST_6_MONTHS:
                stats = new StatsHandler(this.session, StatsHandler.Timespan.MONTH, 6, scope);
                break;
            case LAST_TWO_MONTHS:
                stats = new StatsHandler(this.session, StatsHandler.Timespan.WEEK, 9, scope);
                break;
            case LAST_WEEK:
                stats = new StatsHandler(this.session, StatsHandler.Timespan.DAY, 7, scope);
                break;
            case LAST_48_HOURS:
                stats = new StatsHandler(this.session, StatsHandler.Timespan.HOUR, 48, scope);
                break;
            case LAST_DAY:
                stats = new StatsHandler(this.session, StatsHandler.Timespan.HOUR, 24, scope);
                break;
        }

        return new StatsTask(stats);
    }
}
