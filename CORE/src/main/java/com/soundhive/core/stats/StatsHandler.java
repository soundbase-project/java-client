package com.soundhive.core.stats;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;


public class StatsHandler {
    public enum Timespan {
        //YEAR("year"),
        MONTH("month"),
        WEEK("week"),
        DAY("day"),
        HOUR("hour");

        final private String label;

        Timespan(String label) {
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }

    public enum Scope {
        TRACKS("tracks"),
        USER("users");
        final private String label;

        Scope(String label) {
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }

    private final SessionHandler session;


    private final Scope scope;

    private final Timespan span;

    private final int nbSpan;

    private final String target;




    public StatsHandler(final SessionHandler session,final  Timespan span, final int nbSpan, Scope scope, final String target) {
        this.session = session;
        this.nbSpan = nbSpan;
        this.span = span;
        this.scope = scope;
        this.target = target;
    }


    /**
     * request : localhost:3000/:scope/:target/stats/last/:nb/day
     * @return A response containing the requested stats in an object
     */
    public Response<Stats> queryStats() {
        String request = String.format("%s/%s/stats/last/%d/%s", this.scope.toString(), target, this.nbSpan, this.span.toString());

        return Response.queryResponse(request,
                session.getToken(),
                Stats::new);
    }
}
