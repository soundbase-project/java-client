package com.soundhive.core.stats;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import com.soundhive.core.Enums.*;


public class StatsQueries {

    /**
     * request : localhost:3000/:scope/:target/stats/last/:nb/day
     * @return A response containing the requested stats in an object
     */
    public static Response<Stats> queryStats(final SessionHandler session,final  Timespan span, final int nbSpan, Scope scope, final String target) {
        String request = String.format("%s/%s/stats/last/%d/%s", scope.toString(), target, nbSpan, span.toString());

        return Response.queryResponse(request,
                session.getToken(),
                Stats::new);
    }
}
