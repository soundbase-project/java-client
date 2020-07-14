package com.soundhive.core.response;

public class ResponseParsingError extends Error{
    public ResponseParsingError() { super(); }
    public ResponseParsingError(String message) { super(message); }
    public ResponseParsingError(String message, Throwable cause) { super(message, cause); }
    public ResponseParsingError(Throwable cause) { super(cause); }
}
