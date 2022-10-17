package com.hashedin.loginservice.exception;

public class UserAlreadyLoggedInException extends RuntimeException{
    public UserAlreadyLoggedInException(String message) {
        super(message);
    }
}
