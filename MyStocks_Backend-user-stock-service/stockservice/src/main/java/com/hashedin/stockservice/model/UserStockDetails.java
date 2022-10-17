package com.hashedin.stockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStockDetails {
    @Id
    @GeneratedValue (strategy= GenerationType.SEQUENCE, generator="user_stock_details_sequence")
    @SequenceGenerator(name = "user_stock_details_sequence", sequenceName = "user_stock_details_sequence")
    private Long userStockDetailsId;
    @ManyToOne
    private User user;
    @OneToOne
    private Stock stock;
    @Column
    private String operationType;
    @Column
    private Double stockPrice;
    @Column
    private Double stockQuantity;
    @Column
    private Date currentTodayDate;

    public UserStockDetails(User user, Stock stock, String operationType, Double stockPrice, Double stockQuantity, Date currentTodayDate) {
        this.user = user;
        this.stock = stock;
        this.operationType = operationType;
        this.stockPrice = stockPrice;
        this.stockQuantity = stockQuantity;
        this.currentTodayDate = currentTodayDate;
    }
}
