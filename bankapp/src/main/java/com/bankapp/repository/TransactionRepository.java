package com.bankapp.repository;

import com.bankapp.model.Transaction;
import com.bankapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Date;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // For getPastTransaction
    List<Transaction> findByUser(User user);
    
    // For addTransaction
    @Query(value = "INSERT INTO transactions(user_id, trans_type, trans_amount, trans_date) VALUES(?1, ?2, ?3, GETDATE())", 
           nativeQuery = true)
    void createTransaction(Long userId, String transType, BigDecimal amount);
    
    // // For transfer operations
    // List<Transaction> findByUserAndTransDateBetween(User user, Date startDate, Date endDate);
    
    // For transaction history
    // @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId ORDER BY t.transDate DESC")
    // List<Transaction> findUserTransactions(Long userId);
    
    // // For transaction types
    // List<Transaction> findByTransType(String transType);
    
    // // For date range queries
    // List<Transaction> findByTransDateBetween(Date startDate, Date endDate);
    
    // // For amount range queries
    // List<Transaction> findByTransAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
}