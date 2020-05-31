package com.soundhive.core.conf;

public class MissingParamException extends Exception{
    public MissingParamException() { super(); }
    public MissingParamException(String message) { super(message); }
    public MissingParamException(String message, Throwable cause) { super(message, cause); }
    public MissingParamException(Throwable cause) { super(cause); }
}
