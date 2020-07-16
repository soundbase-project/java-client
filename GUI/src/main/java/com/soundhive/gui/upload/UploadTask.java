package com.soundhive.gui.upload;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import com.soundhive.core.upload.AlbumUpload;
import com.soundhive.core.upload.UploadQueries;
import javafx.concurrent.Task;

public class UploadTask extends Task<Response<Void>> {

    private final SessionHandler session;
private final AlbumUpload albumUpload;

    public UploadTask(final SessionHandler session,final AlbumUpload album) {
        this.session = session;
        this.albumUpload = album;
    }

    @Override
    protected Response<Void> call() {
        return UploadQueries.postAlbum(session, albumUpload);
    }
}
