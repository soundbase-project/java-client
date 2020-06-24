package com.soundhive.core.upload;

import java.io.File;

public class TrackUpload {
    private final String title;
    private final String description;
    private final String genre;
    private final File  trackFile;

    public TrackUpload(final String title, final String description, final String genre, final File trackFile) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.trackFile = trackFile;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public File getTrackFile() {
        return trackFile;
    }
}
