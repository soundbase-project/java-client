package com.soundhive.core.upload;

import kong.unirest.ContentType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUpload {

    private final File file;
    private final ContentType MIMEType;

    public FileUpload(final File file) throws IOException {
        this.file = file;

        String contentType = Files.probeContentType(file.toPath());

        this.MIMEType = ContentType.create(contentType);


    }

    public ContentType getMIMEType() {
        return MIMEType;
    }

    public File getFile() {
        return file;
    }
}
