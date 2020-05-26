package com.soundhive.response;


public class Response<T> {

    public enum Status {
        SUCCESS,
        ERROR,
        UNAUTHENTICATED
    }

    private final Status status;
    private final T content;

    public Response(T content, Status status) {
        this.content = content;
        this.status = status;
    }

    public Response(Status status) {
        this.status = status;
        this.content = null;
    }

    public Status getStatus() {
        return status;
    }

    public T getContent() {
        return content;
    }
}
