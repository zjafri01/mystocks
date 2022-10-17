package com.hashedin.stockservice.repository;

import com.hashedin.stockservice.model.Stock;
import com.hashedin.stockservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
