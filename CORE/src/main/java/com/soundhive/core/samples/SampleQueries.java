package com.soundhive.core.samples;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.InternalRequestError;
import com.soundhive.core.response.Response;
import com.soundhive.core.tracks.Album;
import com.soundhive.core.tracks.Track;
import kong.unirest.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.soundhive.core.response.Response.Status.SUCCESS;

public class SampleQueries {
    public static Response<List<Sample>> queryUserSamples(SessionHandler session) {
        String route = String.format("users/%s/samples", session.getUsername());
        return Response.queryResponse(route,
                session.getToken(),
                jsonObj -> {
                    List<Sample> samples = new ArrayList<>();
                    JSONArray array = jsonObj.getArray();
                    int length = array.length();

                    for (int i = 0; i < length; i++) {
                        Sample sample = new Sample(array.getJSONObject(i));

                        samples.add(sample);
                    }
                    return samples;
                });
    }
}
