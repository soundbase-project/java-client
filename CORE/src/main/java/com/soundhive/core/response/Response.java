package com.soundhive.core.response;


import kong.unirest.*;
import kong.unirest.json.JSONException;

import java.util.Map;

public class Response<T> { //TODO : implement JSON parsing case

    public enum Status {
        SUCCESS,
        UNAUTHENTICATED,
        CONNECTION_FAILED,
        INTERNAL_ERROR,
    }

    private final Status status;
    private final String message;
    private final Throwable exception;
    private final T content;

    private Response(final T content,final  Status status, final String message) {
        this.content = content;
        this.status = status;
        this.message = message;
        this.exception = null;
    }

    public Response(final Status status, final String message) {
        this.status = status;
        this.message = message;
        this.content = null;
        this.exception = null;

    }

    public Response( final Status status, final String message, final Throwable exception) {
        this.status = status;
        this.exception = exception;
        this.content = null;
        this.message = message;
    }

//    public Response(Response<?> response) {
//        this.status = response.getStatus();
//        this.message = response.getMessage();
//        switch (response.getStatus()) {
//            case SUCCESS:
//                this.content = (T) response.getContent();
//                this.exception = null;
//                break;
//
//            case CONNECTION_FAILED:
//                this.exception = response.getException();
//                this.content = null;
//            case INTERNAL_ERROR:
//                this.exception = response.getException();
//            case UNAUTHENTICATED:
//                this.
//        }
//        this.status = response.getStatus();
//        this.message = response.getMessage();
//        this.exception = response.getException();
//        if (response.getStatus() == Status.SUCCESS){
//            this.content = (T) response.getContent();
//        }
//        else {
//            this.content = null;
//        }
//    }

    public Status getStatus() {
        return status;
    }

    public Throwable getException() {
        return exception;
    }

    public T getContent() {

        if (this.status == Status.SUCCESS) {
            return content;
        } else {
            throw new NoContentError("If the status of the response is different than Success, there is no content.");
        }
    }

    public String getMessage() {
        return message;
    }

    public static <T> Response<T> queryResponse(String route, String token, JsonInterpretingFunction<T> cast) { // TODO: USE UNIREST'S "ONSUCCESS", maybe use inheritance
        HttpResponse<JsonNode> res;
        try {
            res = Unirest.get(route)
                    .header("accept", "application/json")
                    .header("authorization", "Bearer " + token)
                    .asJson();

        } catch (Exception e) {
            return new Response<>(Response.Status.CONNECTION_FAILED, "No query response from the server.",e);
        }


        switch (res.getStatus()) {
            case 200:
                try {
                    T responseObject = cast.apply(res.getBody());
                    return new Response<>(responseObject , Response.Status.SUCCESS, res.getStatusText());
                }
                catch (JSONException  e) {
                    return new Response<>(Response.Status.INTERNAL_ERROR,res.getStatusText(), e);
                }
                catch (InternalRequestError e) {
                    Response<?> innerRes = e.getResponse();
                    return new Response<>(innerRes.getStatus(), innerRes.getMessage(), e);
                }
            case 401:
                return new Response<>(Status.UNAUTHENTICATED, res.getStatusText());

            default:
                return  new Response<>(Response.Status.INTERNAL_ERROR, res.getStatusText());


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
                    return new Response<>(Response.Status.INTERNAL_ERROR, e.getMessage());
                }
                catch (InternalRequestError e) {
                    Response<?> innerRes = e.getResponse();
                    return new Response<>(innerRes.getStatus(), innerRes.getMessage(), e);
                }
            case 401:
                return new Response<>(Status.UNAUTHENTICATED, res.getStatusText());

            default:
                return new Response<>(Response.Status.INTERNAL_ERROR, res.getStatusText());


        }
    }
}
