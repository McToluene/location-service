package com.mctoluene.locationservice.exceptions;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message, Exception cause) {
        super(message, cause);
    }

    public InternalServerException(String message) {
        super(message);
    }
}
