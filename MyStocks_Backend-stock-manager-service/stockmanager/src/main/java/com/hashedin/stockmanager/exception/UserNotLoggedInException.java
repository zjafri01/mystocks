package com.hashedin.stockmanager.exception;

public class UserNotLoggedInException extends RuntimeException{
    public UserNotLoggedInException(String message) {
        super(message);
    }
}