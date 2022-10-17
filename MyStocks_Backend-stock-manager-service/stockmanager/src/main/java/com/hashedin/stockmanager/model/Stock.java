package com.hashedin.stockmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    private String stock_id;
    private String stock_name;
    private Double price;
    private Double pe_ratio;
}
