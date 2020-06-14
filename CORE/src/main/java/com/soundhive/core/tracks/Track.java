package com.soundhive.core.tracks;

import kong.unirest.JsonNode;
import kong.unirest.json.JSONObject;

public class Track {

    private final String title;

    private final String description;

    private final String genre;

    private final

    public Track(JSONObject jsonTrack) {
        this.title = jsonTrack.getString("title")


    }
}
