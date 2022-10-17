package com.hashedin.stockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletHistory {
    private Long transactionId;
    private String operationType;
    private Double transactionAmount;
    private Date transactionDate;
}