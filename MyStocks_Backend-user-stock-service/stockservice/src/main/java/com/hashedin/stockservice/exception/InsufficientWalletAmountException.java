package com.hashedin.stockservice.exception;

public class InsufficientWalletAmountException extends RuntimeException{
    public InsufficientWalletAmountException(String message) {
        super(message);
    }
}