package com.soundhive.gui.sample;

import com.soundhive.core.Enums.*;
import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import com.soundhive.core.upload.SampleUpload;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;

public class SamplesUploadService extends Service<Response<?>> {

    private final SessionHandler session;

    private final StringProperty title;

    private final StringProperty description;

    private final Visibility visibility;

    private final License license;

    private final File sampleFile;

    public SamplesUploadService(SessionHandler session, StringProperty title, StringProperty description, Visibility visibility, License license, File sampleFile) {
        this.session = session;
        this.title = title;
        this.description = description;
        this.visibility = visibility;
        this.license = license;
        this.sampleFile = sampleFile;
    }

    @Override
    protected SampleUploadTask createTask() {
        return new SampleUploadTask(session, title.getValue(), description.getValue(), visibility, license, sampleFile);
    }
}
