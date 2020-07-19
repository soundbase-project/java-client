package com.soundhive.gui.sample;

import com.soundhive.core.Enums;
import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import com.soundhive.core.upload.SampleUpload;
import com.soundhive.core.upload.UploadQueries;
import javafx.concurrent.Task;

import java.io.File;

public class SampleUploadTask extends Task<Response<?>> {
    private final SessionHandler session;

    private final String title;

    private final String description;

    private final Enums.Visibility visibility;

    private final Enums.License license;

    private final File sampleFile;


    public SampleUploadTask(SessionHandler session, String title, String description, Enums.Visibility visibility, Enums.License license, File sampleFile) {
        this.session = session;
        this.title = title;
        this.description = description;
        this.visibility = visibility;
        this.license = license;
        this.sampleFile = sampleFile;
    }

    @Override
    protected Response<?> call() throws Exception {
        SampleUpload upload = new SampleUpload(title, description, visibility, license, sampleFile);

        return UploadQueries.postSample(session, upload);
    }
}
