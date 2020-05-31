package com.soundhive.core.conf;

public class ConfigFileException extends Exception{
    public ConfigFileException() { super(); }
    public ConfigFileException(String message) { super(message); }
    public ConfigFileException(String message, Throwable cause) { super(message, cause); }
    public ConfigFileException(Throwable cause) { super(cause); }
}
