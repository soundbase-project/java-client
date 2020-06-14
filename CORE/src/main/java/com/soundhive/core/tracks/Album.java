package com.soundhive.core.tracks;

import kong.unirest.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private List<Track> tracks;
    public Album(JSONObject object) {
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
