package com.soundhive.core.tracks;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.InternalRequestError;
import com.soundhive.core.response.Response;
import kong.unirest.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.soundhive.core.response.Response.Status.*;

public class TracksQueries {

    private static Response<List<Track>> queryTracks(SessionHandler session, Album album) {
        String request = String.format("albums/%s/tracks", album.getID());
        return Response.queryResponse(request,
                session.getToken(),
                node -> {
                    List<Track> tracks = new ArrayList<>();
                    JSONArray array = node.getObject().getJSONArray("items");
                    int length = array.length();
                    for (int i = 0; i < length; i++) {
                        tracks.add(new Track(array.getJSONObject(i)));
                    }
                    return tracks;
                });
    }

    public static Response<List<Album>> queryAlbums(SessionHandler session) {
        String request = String.format("users/%s/albums", session.getUsername());
        return Response.queryResponse(request,
                session.getToken(),
                node -> {
                    List<Album> albums = new ArrayList<>();
                    JSONArray array = node.getObject().getJSONArray("items");
                    int length = array.length();

                    for (int i = 0; i < length; i++) {
                        Album album = new Album(array.getJSONObject(i));
                        Response<List<Track>> tracks = queryTracks(session, album);
                        if (tracks.getStatus() != SUCCESS) {
                            throw new InternalRequestError(tracks.getException(), tracks);
                        }
                        album.setTracks(tracks.getContent());
                        albums.add(album);
                    }
                    return albums;
                });
    }


}
