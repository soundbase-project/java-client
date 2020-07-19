package com.soundhive.core.upload;

import com.soundhive.core.Enums.*;
import com.soundhive.core.response.InternalRequestError;

import java.io.File;
import java.io.IOException;

public class SampleUpload {
    private final String title;

    private final String description;

    private final Visibility visibility;

    private  final License license;

    private final File sampleFile;

    public SampleUpload(final String title, final String description, final Visibility visibility, final License license, final File sampleFile) throws InvalidUploadException{
        this.title = title;
        this.description = description;
        this.visibility = visibility;
        this.license = license;
        this.sampleFile = sampleFile;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public License getLicense() {
        return license;
    }

    public File getSampleFile() {
        return sampleFile;
    }
}
