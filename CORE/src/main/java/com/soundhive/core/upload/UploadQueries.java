package com.soundhive.core.upload;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.InternalRequestError;
import com.soundhive.core.response.Response;
import org.javatuples.Pair;

import java.util.HashMap;

public class UploadQueries {

    public static Response<Void>  postAlbum(final SessionHandler session, final AlbumUpload album) {

        return Response.postResponse(
                "albums",
                session.getToken(),
                new HashMap<>() {
                    {
                        put("title", album.getTitle());
                        put("description", album.getDescription());
                    }
                },
                new Pair<>("cover_file", album.getCoverFile())
                ,
                node -> {
                    for (TrackUpload track :
                            album.getTracks()) {
                        Response<?> res = postTrack(session, node.getObject().getString("id"), track);
                        if (!res.getStatus().equals(Response.Status.SUCCESS)){
                            throw new InternalRequestError("Could not upload track : " + track.getTitle(), res);
                        }

                    }
                }
        );
    }

    private static Response<Void> postTrack(SessionHandler session, String albumID, TrackUpload track) {
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
                new Pair<>("track_file", track.getTrackFile())
                ,
                null
        );

    }
}
