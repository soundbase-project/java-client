package com.soundhive.core.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlbumUpload {
    private final String title;
    private final String description;
    private final File coverFile;
    private final List<TrackUpload> tracks;

    public AlbumUpload(final String title, final String description, final File coverFile) {
        this.title = title;
        this.description = description;
        this.coverFile = coverFile;
        this.tracks = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public File getCoverFile() {
        return coverFile;
    }

    public List<TrackUpload> getTracks() {
        return tracks;
    }
}
