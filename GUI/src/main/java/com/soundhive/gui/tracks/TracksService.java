package com.soundhive.gui.tracks;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import com.soundhive.core.tracks.Album;
import javafx.concurrent.Service;

import java.util.List;

public class TracksService  extends Service<Response<List<Album>>> {
    private final SessionHandler session;

    public TracksService(SessionHandler session) {
        this.session = session;
    }

    @Override
    protected TracksTask createTask() {
        return new TracksTask(session);
    }
}
