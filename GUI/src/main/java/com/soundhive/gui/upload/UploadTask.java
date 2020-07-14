package com.soundhive.gui.upload;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import com.soundhive.core.upload.AlbumUpload;
import com.soundhive.core.upload.TrackUpload;
import com.soundhive.core.upload.UploadHandler;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import java.io.File;
import java.util.List;

public class UploadTask extends Task<Response<Void>> {

    private final SessionHandler session;
private final AlbumUpload albumUpload;

    public UploadTask(final SessionHandler session,final AlbumUpload album) {
        this.session = session;
        this.albumUpload = album;
    }

    @Override
    protected Response<Void> call() {
        UploadHandler uploader = new UploadHandler(session, albumUpload);
        return uploader.postAlbum();
    }
}
