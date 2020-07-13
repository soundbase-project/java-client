package com.soundhive.core.tracks;

import kong.unirest.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private List<Track> tracks;
    private final String ID;
    private final String title;
    private final String coverFile;

    private String description;

    public Album(JSONObject object) {
        this.ID = object.getString("id");
        this.coverFile = object.getString("cover_filename");
        this.title = object.getString("title");
        if (object.has("description")) {
            this.description = object.getString("description");
        }
    }

    public String getDescription() {
        return this.description != null ? description : "";
    }

    public String getID() {
        return ID;
    }

    public String getCoverFile() {
        return coverFile;
    }

    public String getTitle() {
        return title;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(final List<Track> tracks) {
        this.tracks = tracks;
    }
}
