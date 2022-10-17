package com.hashedin.stockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue (strategy= GenerationType.SEQUENCE, generator="transaction_sequence")
    @SequenceGenerator(name = "transaction_sequence", sequenceName = "transaction_sequence")
    private Long transactionId;

    @OneToOne
    private User user;

    @OneToOne
    private Order order;

    @Column
    private Date transactionDate;

    @Column
    private Double transactionAmount;

    @Column
    private String operationType;

    @Column
    private String transactionStatus;

    @Transient
    private Double quantity;
    @Transient
    private Integer stockId;

    public Transaction(User user, Order order, Date transactionDate, Double transactionAmount, String operationType, String transactionStatus) {
        this.user = user;
        this.order = order;
        this.transactionDate = transactionDate;
        this.transactionAmount = transactionAmount;
        this.operationType = operationType;
        this.transactionStatus = transactionStatus;
    }
}
