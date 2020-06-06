package com.soundhive.core.response;


public class Response<T> {

    public enum Status {
        SUCCESS,
        UNAUTHENTICATED,
        CONNECTION_FAILED,
        UNKNOWN_ERROR,
    }

    private final Status status;
    private final String message;
    private final T content;

    public Response(final T content,final  Status status, final String message) {
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
}
