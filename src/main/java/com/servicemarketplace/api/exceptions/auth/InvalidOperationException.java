package com.servicemarketplace.api.exceptions.auth;

public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String message) {
        super(message);
    }
}
