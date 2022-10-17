package com.hashedin.stockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Order_Table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue (strategy= GenerationType.SEQUENCE, generator="order_sequence")
    @SequenceGenerator(name = "order_sequence", sequenceName = "order_sequence")
    private Long orderId;
    @ManyToOne
    private Stock stock;
    @Column
    private Double quantity;
    @Column
    private Date operationDate;
    @Column
    private String operationType;
    @Column
    private String orderStatus;
    @Column
    private Double orderAmount;

    public Order(Stock stock, Double quantity, Date operationDate, String operationType, Double orderAmount) {
        this.stock = stock;
        this.quantity = quantity;
        this.operationDate = operationDate;
        this.operationType = operationType;
        this.orderAmount = orderAmount;
    }
}
