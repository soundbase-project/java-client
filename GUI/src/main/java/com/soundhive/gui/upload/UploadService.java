package com.soundhive.gui.upload;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import com.soundhive.core.upload.AlbumUpload;
import com.soundhive.core.upload.InvalidUploadException;
import com.soundhive.core.upload.TrackUpload;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;

import java.io.File;
import java.util.List;

public class UploadService extends Service<Response<Void>> {

    private final SessionHandler session;

    private final AlbumUpload albumUpload;

    public UploadService(final SessionHandler session, final StringProperty title, final StringProperty description, final File coverFile, final List<TrackUpload> tracks)  throws InvalidUploadException {
        if (tracks == null || tracks.size() < 1) {
            throw new InvalidUploadException("There are no track in this album.");
        }
        this.session = session;
        this.albumUpload = new AlbumUpload(title.getValue(), description.getValue(), coverFile);
        this.albumUpload.getTracks().addAll(tracks);
    }
    @Override
    protected UploadTask createTask() {
        return new UploadTask(session, albumUpload);
    }
}
