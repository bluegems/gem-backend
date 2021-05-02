package com.bluegems.server.gembackend.exception;

public class BadTokenException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Token is invalid or expired!";
    }
}
