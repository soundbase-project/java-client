package com.soundhive.core.upload;

import java.io.File;

public class TrackUpload {
    public enum License{
        ALL_RIGHTS_RESERVED("All right reserved"),
        CC("Creative Common");

        final private String label;

        License(String label) {
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }

    private final String title;
    private final String description;
    private final String genre;
    private final File  trackFile;
    private final License license;
    private final boolean downloadable;

    public TrackUpload(final String title, final String description,
                       final String genre, final File trackFile,
                       final License license, final boolean downloadable) {
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
