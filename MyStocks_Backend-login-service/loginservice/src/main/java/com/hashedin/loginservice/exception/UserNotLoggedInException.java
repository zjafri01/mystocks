package com.hashedin.loginservice.exception;

public class UserNotLoggedInException extends RuntimeException{
    public UserNotLoggedInException(String message) {
        super(message);
    }
}