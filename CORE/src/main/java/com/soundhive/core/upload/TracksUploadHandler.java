package com.soundhive.core.upload;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.InternalRequestError;
import com.soundhive.core.response.Response;

import java.util.HashMap;

public class TracksUploadHandler {

    private final  SessionHandler session;
    private final AlbumUpload album;

    public TracksUploadHandler(final SessionHandler session, final AlbumUpload album) {
        this.session = session;
        this.album = album;
    }

    public Response<Void> postAlbum() {

        return Response.postResponse(
                "/albums",
                session.getToken(),
                new HashMap<>() {
                    {
                        put("title", album.getTitle());
                        put("description", album.getDescription());
                        put("coverFile", album.getCoverFile());
                    }
                },
                object -> {
                    for (TrackUpload track :
                            this.album.getTracks()) {
                        Response<?> res = postTrack(track);
                        if (!res.getStatus().equals(Response.Status.SUCCESS)){
                            throw new InternalRequestError("Could not upload track : " + track.getTitle(), res);
                        }

                    }
                }
        );
    }

    public Response<Void> postTrack(TrackUpload track) {
        return Response.postResponse(
                "/tracks",
                session.getToken(),
                new HashMap<>() {
                    {
                        put("title", track.getTitle());
                        put("description", track.getDescription());
                        put("genre", track.getGenre());
                        put("trackFile", track.getTrackFile());
                    }
                },
                null
        );

    }
}
