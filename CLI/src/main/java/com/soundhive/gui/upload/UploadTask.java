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
    private final String title;
    private final String description;
    private  final File coverFile;
    private final List<TrackUpload> tracks;


    public UploadTask(final SessionHandler session, final String title, final String description, final File coverFile, final List<TrackUpload> tracks) {
        this.title = title;
        this.description = description;
        this.coverFile = coverFile;
        this.tracks = tracks;
        this.session = session;
    }

    @Override
    protected Response<Void> call() {
        AlbumUpload album = new AlbumUpload(this.title, this.description, this.coverFile);
        album.getTracks().addAll(this.tracks);
        UploadHandler uploader = new UploadHandler(session, album);
        return uploader.postAlbum();
    }
}
