package com.soundhive.core.upload;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.InternalRequestError;
import com.soundhive.core.response.Response;
import org.javatuples.Pair;

import java.io.File;
import java.util.HashMap;

public class UploadHandler {

    private final  SessionHandler session;
    private final AlbumUpload album;

    public UploadHandler(final SessionHandler session, final AlbumUpload album) {
        this.session = session;
        this.album = album;
    }

    public Response<Void> postAlbum() {

        return Response.postResponse(
                "albums",
                session.getToken(),
                new HashMap<>() {
                    {
                        put("title", album.getTitle());
                        put("description", album.getDescription());
                    }
                },
                new Pair<>("coverFile", album.getCoverFile())
                ,
                node -> {
                    for (TrackUpload track :
                            this.album.getTracks()) {
                        Response<?> res = postTrack(node.getObject().getString("id"), track);
                        if (!res.getStatus().equals(Response.Status.SUCCESS)){
                            throw new InternalRequestError("Could not upload track : " + track.getTitle(), res);
                        }

                    }
                }
        );
    }

    public Response<Void> postTrack(String albumID, TrackUpload track) {
        return Response.postResponse(
                "tracks",
                session.getToken(),
                new HashMap<>() {
                    {
                        put("title", track.getTitle());
                        put("description", track.getDescription());
                        put("genre", track.getGenre());
                        put("album", albumID);
                        put("license", track.getLicense().toString());
                        put("downloadable", track.isDownloadable());
                    }
                },
                new Pair<>("trackFile", track.getTrackFile())
                ,
                null
        );

    }
}
