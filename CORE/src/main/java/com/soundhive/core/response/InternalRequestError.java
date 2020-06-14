package com.soundhive.core.response;

public class InternalRequestError extends Error{
    private Response<?> response;
    public InternalRequestError(Response<?> response) { super(); }
    public InternalRequestError(String message, Response<?> response) { super(message); }
    public InternalRequestError(String message, Throwable cause, Response<?> response) { super(message, cause); }
    public InternalRequestError(Throwable cause, Response<?> response) { super(cause); }

    public Response.Status getRequestStatus() {
        return this.response.getStatus();
    }

    public String getRequestMessage() {
        return this.response.getMessage();
    }


}
