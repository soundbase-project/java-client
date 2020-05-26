package com.soundhive.stats;

import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;

public class Keyframe {

    private final int plays;
    //private final int scans; TODO: not implemented in API
    private final String period;

    public Keyframe(JSONObject keyframe) throws JSONException {
        this.plays = keyframe.getInt("count");
        this.period = keyframe.getString("period");
    }

    public int getPlays() {
        return plays;
    }

    //public int getScans() {
    //    return scans;
    //}

    public String getPeriod() {
        return period;
    }
}
