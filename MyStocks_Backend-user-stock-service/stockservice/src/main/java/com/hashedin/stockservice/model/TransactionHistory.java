package com.hashedin.stockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistory {
    private Long transactionId;
    private String operationType;
    private Double transactionAmount;
    private Date transactionDate;
    private Double quantity;
    private String stockName;
    private Double stockPrice;
    private Stock stock;
}
