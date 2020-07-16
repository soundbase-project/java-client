package com.soundhive.core.upload;

import com.soundhive.core.GenericDeclarations.License;

import java.io.File;

public class TrackUpload {

    private final String title;
    private final String description;
    private final String genre;
    private final File  trackFile;
    private final License license;
    private final boolean downloadable;

    public TrackUpload(final String title, final String description,
                       final String genre, final File trackFile,
                       final License license, final boolean downloadable) throws InvalidUploadException{
        if (title.isEmpty()){
            throw new InvalidUploadException("No title provided for a track.");
        }

        if (genre.isEmpty()){
            throw new InvalidUploadException("No genre provided for the track : " + title);
        }

        if (description.isEmpty()){
            throw new InvalidUploadException("No description provided for the track : " + title);
        }

        if (license.toString().isEmpty()){
            throw new InvalidUploadException("No license provided for the track : " + title);
        }

        if (trackFile == null){
            throw new InvalidUploadException("No file provided for the track : " + title);
        }
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.trackFile = trackFile;
        this.license = license;
        this.downloadable = downloadable;
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

    public License getLicense() {
        return license;
    }

    public boolean isDownloadable() {
        return downloadable;
    }
}
