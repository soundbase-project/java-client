package com.soundhive.core.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlbumUpload {
    private final String title;
    private final String description;
    private final File coverFile;
    private final List<TrackUpload> tracks;

    public AlbumUpload(final String title, final String description, final File coverFile) throws InvalidUploadException{
        if (title.isEmpty() || title.isBlank()) {
            throw new InvalidUploadException("The album must have a title.");
        }

        if (description.isEmpty() || description.isBlank()) {
            throw new InvalidUploadException("The album must have a description.");
        }

        if (coverFile == null  || !coverFile.exists()) {
            throw new InvalidUploadException("the album must have a valid cover file.");
        }

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
