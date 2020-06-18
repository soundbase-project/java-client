package com.soundhive.core.response;

public class NoContentError extends Error{
    public NoContentError() { super(); }
    public NoContentError(String message) { super(message); }
    public NoContentError(String message, Throwable cause) { super(message, cause); }
    public NoContentError(Throwable cause) { super(cause); }
}
