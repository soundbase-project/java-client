package com.soundhive.core.response;

import kong.unirest.JsonNode;
import kong.unirest.json.JSONException;

@FunctionalInterface
public interface JsonInterpretingFunction<T>{
    T apply(JsonNode object) throws JSONException;

}
