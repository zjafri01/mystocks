package com.hashedin.stockservice.exception;

import com.hashedin.stockservice.model.StockBuySellModel;

public class InsufficientStocksAvailableException extends RuntimeException{
    public InsufficientStocksAvailableException(String message) {
        super(message);
    }
}
