package com.soundhive.core.samples;

import com.soundhive.core.GenericDeclarations;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;

public class Sample {

    private final String title;

    private final String description;

    private final GenericDeclarations.Visibility visibility;

    public Sample(final JSONObject sample) throws JSONException {

        this.title = sample.getString("title");

        this.description = sample.getString("description");

        this.visibility = GenericDeclarations.Visibility.valueOf(sample.getString("visibility"));

    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public GenericDeclarations.Visibility getVisibility() {
        return visibility;
    }

}