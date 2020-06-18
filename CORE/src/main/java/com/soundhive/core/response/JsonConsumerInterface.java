package com.soundhive.core.response;

import kong.unirest.JsonNode;
import kong.unirest.json.JSONException;

@FunctionalInterface
public interface JsonConsumerInterface {
    void accept(JsonNode node) throws JSONException;
}
