package com.basic.basic_authorization_api.exceptions;

public class PasswordMismatchError extends Exception {

    public PasswordMismatchError(String message) {
        super(message);
    }
}
