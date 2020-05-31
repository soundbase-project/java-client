package com.soundhive.core.stats;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Stats {
    private final int listenings;

    //private final int skillNotes;

    //private final int downloads;

    //private final int scans;

    private final List<Keyframe> keyframes;

    public Stats(JSONObject res) throws JSONException {
        this.keyframes = new ArrayList<>();
        JSONArray array = res.getJSONArray("keyframes");

        for (int i = 0; i < array.length(); i++){
            this.keyframes.add(new Keyframe(array.getJSONObject(i)));
        }

        this.listenings = res.getInt("listenings");

    }

    public List<Keyframe> getKeyframes() {
        return this.keyframes;
    }
}
