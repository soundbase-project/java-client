package com.soundhive.core.response;

public class InternalRequestError extends Error{
    private Response<?> response;
    public InternalRequestError(Response<?> response) { super(); this.response = response; }
    public InternalRequestError(String message, Response<?> response) { super(message);  this.response = response; }
    public InternalRequestError(String message, Throwable cause, Response<?> response) { super(message, cause);  this.response = response; }
    public InternalRequestError(Throwable cause, Response<?> response) { super(cause);  this.response = response; }

    public Response<?> getResponse() {
        return response;
    }
}
