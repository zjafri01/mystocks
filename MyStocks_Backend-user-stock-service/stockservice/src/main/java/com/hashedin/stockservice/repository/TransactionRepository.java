package com.hashedin.stockservice.repository;

import com.hashedin.stockservice.model.Transaction;
import com.hashedin.stockservice.model.TransactionHistory;
import com.hashedin.stockservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    //select stock_quantity from user_stock_details where stock_stock_id=:stock_id and user_user_id=:user_id order by current_today_date DESC LIMIT 1
    @Query(nativeQuery = true,value = "select transaction_table.transaction_id, transaction_table.operation_type, transaction_table.transaction_amount, transaction_table.transaction_date, order_table.quantity, order_table.stock_stock_id " +
            "from transaction_table JOIN order_table ON transaction_table.order_order_id=order_table.order_id " +
            "where transaction_table.user_user_id=:user_id and (transaction_table.operation_type=\"buy\" or transaction_table.operation_type=\"sell\") order by transaction_table.transaction_date DESC LIMIT 10;\n")
    public List<Transaction> findLatestUserTransactions(@Param("user_id") Long user_id);


    public List<Transaction> findTransactionByUser(User user);
}
