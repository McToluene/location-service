package com.mctoluene.locationservice.exceptions;

public class PageableException extends RuntimeException {

    public PageableException(String message, Exception cause) {
        super(message, cause);
    }

    public PageableException(String message) {
        super(message);
    }
}
