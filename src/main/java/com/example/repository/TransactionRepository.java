package com.example.crm.repository;

import com.example.crm.model.Seller;
import com.example.crm.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    void deleteBySeller(Seller seller);

    List<Transaction> findBySeller(Seller seller);


    @Query("SELECT t.seller FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY t.seller HAVING SUM(t.amount) < :amount AND SUM(t.amount) > 0")
    List<Seller> findSellersWithTransactionAmountLessThan(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("amount") BigDecimal amount);

    @Query("SELECT t.seller FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY t.seller ORDER BY SUM(t.amount) DESC")
    List<Seller> findTopSellerByPeriod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}
