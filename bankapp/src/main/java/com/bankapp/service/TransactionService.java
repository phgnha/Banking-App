package com.bankapp.service;

import com.bankapp.model.Transaction;
import com.bankapp.model.User;
import java.math.BigDecimal;
import java.util.List;
import java.sql.Date;

public interface TransactionService {
    List<Transaction> getPastTransactions(User user);
    boolean addTransaction(Transaction transaction);
    // boolean transfer(User fromUser, String toUsername, BigDecimal amount);
    // List<Transaction> findByDateRange(Date startDate, Date endDate);
    // List<Transaction> findByAmountRange(BigDecimal minAmount, BigDecimal maxAmount);
}