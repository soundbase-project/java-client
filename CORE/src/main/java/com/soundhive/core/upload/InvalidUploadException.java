package com.soundhive.core.upload;


public class InvalidUploadException extends Exception{
    public InvalidUploadException() { super(); }
    public InvalidUploadException(String message) { super(message); }
    public InvalidUploadException(String message, Throwable cause) { super(message, cause); }
    public InvalidUploadException(Throwable cause) { super(cause); }
}


