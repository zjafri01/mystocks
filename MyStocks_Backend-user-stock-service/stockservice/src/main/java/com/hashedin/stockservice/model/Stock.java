package com.hashedin.stockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "stock_details")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Stock {
    @Id
    private Integer stockId;
    @Column
    private String stockName;
    @Column
    private Double price;
    @Column
    private Double peRatio;
}
