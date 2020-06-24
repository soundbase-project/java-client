package com.soundhive.gui.upload;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import com.soundhive.core.upload.AlbumUpload;
import com.soundhive.core.upload.TrackUpload;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;

import java.io.File;
import java.util.List;

public class UploadService extends Service<Response<Void>> {

    private final SessionHandler session;

    private final StringProperty title;
    private final StringProperty description;
    private final File coverFile;
    private final List<TrackUpload> tracks;

    public UploadService(final SessionHandler session, final StringProperty title, final StringProperty description, final File coverFile, final List<TrackUpload> tracks) {
        this.session = session;
        this.title = title;
        this.description = description;
        this.coverFile = coverFile;
        this.tracks = tracks;

    }
    @Override
    protected UploadTask createTask() {
        return new UploadTask(session, title.getValue(), description.getValue(), coverFile, tracks);
    }
}
