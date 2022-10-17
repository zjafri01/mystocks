package com.hashedin.stockservice.repository;

import com.hashedin.stockservice.model.Order;
import com.hashedin.stockservice.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDetailsRepository extends JpaRepository<Order, Long> {
    //public List<Order> findOrderByStock(Stock stock);
}
