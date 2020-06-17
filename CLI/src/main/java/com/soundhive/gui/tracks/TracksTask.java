package com.soundhive.gui.tracks;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import com.soundhive.core.tracks.Album;
import com.soundhive.core.tracks.TracksHandler;
import javafx.concurrent.Task;

import java.util.List;

public class TracksTask extends Task<Response<List<Album>>> {
    final SessionHandler session;
    public TracksTask(final SessionHandler session) {
        this.session = session;
    }

    @Override
    protected Response<List<Album>> call() {
        return new TracksHandler(session).queryAlbums();
    }
}
