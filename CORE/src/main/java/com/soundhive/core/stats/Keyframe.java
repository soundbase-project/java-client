package com.soundhive.core.stats;

import com.soundhive.core.response.ResponseParsingError;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Keyframe {

    private final int plays;
    //private final int scans; TODO: not implemented in API
    private final Date period;

    public Keyframe(JSONObject keyframe) throws JSONException {
        this.plays = keyframe.getInt("count");

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.SSS'Z'");
        try {
            this.period = formatter.parse(keyframe.getString("period"));
        } catch (ParseException | IllegalArgumentException e) {
            throw new ResponseParsingError("Could not parse the API's dates.", e);
        }
    }

    public int getPlays() {
        return plays;
    }

    //public int getScans() {
    //    return scans;
    //}

    public Date getPeriod() {
        return period;
    }

    public String getPeriodAsStr() {
        return new SimpleDateFormat("dd/MM/yyyy \n hh:mm aa").format(this.period);
    }
}
