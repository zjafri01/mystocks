package com.hashedin.stockservice.exception;

public class StockDoesNotExists extends RuntimeException{
    public StockDoesNotExists(String message) {
        super(message);
    }
}
