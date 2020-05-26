package com.soundhive.stats;

import com.soundhive.authentication.SessionHandler;
import com.soundhive.response.Response;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;


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

    //private Stats stats;

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
        HttpResponse<JsonNode> res = Unirest.get(request)
                .header("accept", "application/json")
                .header("authorization", "Bearer " + session.getToken())
                .asJson();
        switch (res.getStatus()) {
            case 200:
                Stats stats;
                try {
                    stats = new Stats(res.getBody().getObject());
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    return new Response<>(Response.Status.ERROR);
                }
                return new Response<>(stats , Response.Status.SUCCESS);
            case 400:
                System.err.println("Bad request : " + request);
                return  new Response<>(Response.Status.ERROR);
            default:
                return  new Response<>(Response.Status.ERROR);


        }
    }
}
