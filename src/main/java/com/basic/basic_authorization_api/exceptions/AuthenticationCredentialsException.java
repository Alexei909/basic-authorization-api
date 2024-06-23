package com.basic.basic_authorization_api.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationCredentialsException extends AuthenticationException {

    public AuthenticationCredentialsException(String msg) {
        super(msg);
    }

    public AuthenticationCredentialsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
