package com.hashedin.stockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockBuySellModel {
    private String username;
    private Integer stockId;
    private String stockName;
    private Double quantity;
    private Double price;
    private String operationDate;
    private String operationType;
    private Double peRatio;
    private String status;
    private String responseMessage;
    private Integer statusCode;
}
