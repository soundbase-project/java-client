package com.soundhive.core.response;


import kong.unirest.*;
import kong.unirest.json.JSONException;

import java.util.Map;

public class Response<T> { //TODO : implement JSON parsing case

    public enum Status {
        SUCCESS,
        UNAUTHENTICATED,
        CONNECTION_FAILED,
        UNKNOWN_ERROR,
    }

    private final Status status;
    private final String message;
    private final T content;

    private Response(final T content,final  Status status, final String message) {
        this.content = content;
        this.status = status;
        this.message = message;
    }

    public Response(Status status, String message) {
        this.status = status;
        this.content = null;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public T getContent() {
        return content;
    }

    public String getMessage() {
        return message;
    }

    public static <T> Response<T> queryResponse(String route, String token, JsonInterpretingFunction<T> cast) {
        HttpResponse<JsonNode> res;
        try {
            res = Unirest.get(route)
                    .header("accept", "application/json")
                    .header("authorization", "Bearer " + token)
                    .asJson();

        } catch (Exception e) {
            return new Response<>(Response.Status.CONNECTION_FAILED, e.getMessage());
        }

        switch (res.getStatus()) {
            case 200:
                try {
                    T object = cast.apply(res.getBody());
                    return new Response<>(object , Response.Status.SUCCESS, res.getStatusText());
                }
                catch (JSONException e) {
                    return new Response<>(Response.Status.UNKNOWN_ERROR,e.getMessage());
                }
            case 401:
                return new Response<>(Status.UNAUTHENTICATED, res.getStatusText());

            default:
                return  new Response<>(Response.Status.UNKNOWN_ERROR, res.getStatusText());


        }

    }

    public static <T> Response<T> postResponse(String route, String token, Map<String, Object> fields, JsonConsumerInterface cast) {
        HttpResponse<JsonNode> res;
        try {
            res = Unirest.post(route)
                    .header("accept", "application/json")
                    .header("authorization", "Bearer " + token)
                    .fields(fields)
                    .asJson();

        } catch (Exception e) {
            return new Response<>(Response.Status.CONNECTION_FAILED, e.getMessage());
        }

        switch (res.getStatus()) {
            case 200:
                try {
                    cast.accept(res.getBody());
                    return new Response<>(Response.Status.SUCCESS, res.getStatusText());
                } catch (JSONException e) {
                    return new Response<>(Response.Status.UNKNOWN_ERROR, e.getMessage());
                }
                catch (InternalRequestError e) {
                    return new Response<>(e.getRequestStatus(), e.getRequestMessage());
                }
            case 401:
                return new Response<>(Status.UNAUTHENTICATED, res.getStatusText());

            default:
                return new Response<>(Response.Status.UNKNOWN_ERROR, res.getStatusText());


        }
    }
}
