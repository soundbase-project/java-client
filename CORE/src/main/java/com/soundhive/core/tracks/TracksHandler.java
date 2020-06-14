package com.soundhive.core.tracks;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.InternalRequestError;
import com.soundhive.core.response.Response;
import kong.unirest.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.soundhive.core.response.Response.Status.*;

public class TracksHandler {
    private final SessionHandler session;
    public TracksHandler(final SessionHandler session) {
        this.session = session;

    }

    private Response<List<Track>> queryTracks(Album album) {
        String request = String.format("albums/%s/tracks", album);
        return Response.queryResponse(request,
                session.getToken(),
                node -> {
                    List<Track> tracks = new ArrayList<>();
                    JSONArray array = node.getArray();
                    int length = array.length();
                    for (int i = 0; i < length; i++) {
                        tracks.add(new Track(array.getJSONObject(i)));
                    }
                    return tracks;
                });
    }

    public Response<List<Album>> queryAlbums() {
        String request = String.format("users/%s/albums", session.getUsername());
        return Response.queryResponse(request,
                session.getToken(),
                node -> {
                    List<Album> list = new ArrayList<>();
                    JSONArray array = node.getArray();
                    int length = array.length();

                    for (int i = 0; i < length; i++) {
                        Album album = new Album(array.getJSONObject(i));
                        Response<List<Track>> tracks = queryTracks(album);
                        if ( tracks.getStatus() != SUCCESS) {
                            throw new InternalRequestError(tracks);
                        }
                        album.setTracks(tracks.getContent());
                        list.add(album);
                    }

                    return list;
                });
    }


}
