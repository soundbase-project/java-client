package com.soundhive.core.stats;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONException;


public class StatsHandler {
    public enum Timespan {
        YEAR("year"),
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




    public StatsHandler(SessionHandler session, Timespan span, int nbSpan, Scope scope ) {
        this.session = session;
        this.nbSpan = nbSpan;
        this.span = span;
        this.scope = scope;
    }

    //localhost:3000/users/:username/stats/last/:nb/day
    public Response<Stats> queryStats() {
        String request = String.format("%s/%s/stats/last/%d/%s", this.scope.toString(), session.getUsername(), this.nbSpan , this.span.toString());
        HttpResponse<JsonNode> res;
        try {
             res = Unirest.get(request)
                    .header("accept", "application/json")
                    .header("authorization", "Bearer " + session.getToken())
                    .asJson();

        } catch (Exception e) {
            return new Response<>(Response.Status.CONNECTION_FAILED, e.getMessage());
        }

        switch (res.getStatus()) {
            case 200:
                Stats stats;
                try {
                    stats = new Stats(res.getBody().getObject());
                }
                catch (JSONException e) {
                    return new Response<>(Response.Status.UNKNOWN_ERROR,e.getMessage());
                }
                return new Response<>(stats , Response.Status.SUCCESS, res.getStatusText());
            default:
                return  new Response<>(Response.Status.UNKNOWN_ERROR, res.getStatusText());


        }
    }
}
