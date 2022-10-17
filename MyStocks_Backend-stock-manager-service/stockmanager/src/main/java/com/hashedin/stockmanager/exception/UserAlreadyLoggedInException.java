package com.hashedin.stockmanager.exception;

public class UserAlreadyLoggedInException extends RuntimeException{
    public UserAlreadyLoggedInException(String message) {
        super(message);
    }
}
