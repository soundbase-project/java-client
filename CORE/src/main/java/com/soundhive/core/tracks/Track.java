package com.soundhive.core.tracks;

import kong.unirest.json.JSONObject;

public class Track {

    private final String title;

    private final String description;

    private final String genre;

    private final String ID;

    private final int listens;

    public Track(JSONObject jsonTrack) {
        this.title = jsonTrack.getString("title");
        this.description = jsonTrack.getString("description");
        this.genre = jsonTrack.getString("genre");
        this.ID = jsonTrack.getString("id");
        this.listens = jsonTrack.getInt("listeningCount");
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public String getID() {
        return ID;
    }

    public int getListens() {
        return listens;
    }
}
