package com.hashedin.stockservice.repository;

import com.hashedin.stockservice.model.Stock;
import com.hashedin.stockservice.model.User;
import com.hashedin.stockservice.model.UserStockDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserStockDetailsRepository extends JpaRepository<UserStockDetails, Long> {

    public List<UserStockDetails> findUserStockDetailsByUser(User user);

    //@Query()
    //select stock_quantity from user_stock_details where stock_stock_id=2 and user_user_id=1 order by current_today_date DESC;
    //public UserStockDetails findFirstUserStockDetailsByUserAndStockDesc(User user, Stock stock);

    @Query(nativeQuery = true,value = "select stock_quantity from user_stock_details where stock_stock_id=:stock_id and user_user_id=:user_id order by current_today_date DESC LIMIT 1")
    public Double findCurrentUserStockQuantity(@Param("stock_id") Integer stock_id, @Param("user_id") Long user_id);

    //public List<UserStockDetails> findUserStockDetailsByUser(User user);

    /*@Query(nativeQuery = true,value = "SELECT * FROM product p WHERE p.productCategory=:category")
    public List<Product> viewAllProductsByProductCategory(@Param("category") String category);*/
}
