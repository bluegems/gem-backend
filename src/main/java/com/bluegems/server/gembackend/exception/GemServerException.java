package com.bluegems.server.gembackend.exception;

public class GemServerException extends RuntimeException {

    public GemServerException(String message) {
        super(message);
    }

    public GemServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public GemServerException(Throwable cause) {
        super(cause);
    }
}
