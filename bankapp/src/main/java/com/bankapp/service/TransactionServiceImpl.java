package com.bankapp.service;

import com.bankapp.model.Transaction;
import com.bankapp.model.User;
import com.bankapp.repository.TransactionRepository;
import com.bankapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Transaction> getPastTransactions(User user) {
        return transactionRepository.findByUser(user);
    }

    @Override
    @Transactional
    public boolean addTransaction(Transaction transaction) {
        try {
            transactionRepository.save(transaction);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // @Override
    // @Transactional
    // public boolean transfer(User fromUser, String toUsername, BigDecimal amount) {
    //     Optional<User> toUserOpt = userRepository.findByUsername(toUsername);
    //     if (!toUserOpt.isPresent()) {
    //         return false;
    //     }

    //     User toUser = toUserOpt.get();
    //     Date now = new Date(System.currentTimeMillis());

    //     // Create transactions
    //     Transaction sentTrans = new Transaction(fromUser, "Transfer", amount.negate(), now);
    //     Transaction receivedTrans = new Transaction(toUser, "Transfer", amount, now);

    //     // Update balances
    //     fromUser.setCurrentBalance(fromUser.getCurrentBalance().subtract(amount));
    //     toUser.setCurrentBalance(toUser.getCurrentBalance().add(amount));

    //     try {
    //         // Save all changes
    //         userRepository.save(fromUser);
    //         userRepository.save(toUser);
    //         transactionRepository.save(sentTrans);
    //         transactionRepository.save(receivedTrans);
    //         return true;
    //     } catch (Exception e) {
    //         return false;
    //     }
    // }

    // @Override
    // public List<Transaction> findByDateRange(Date startDate, Date endDate) {
    //     return transactionRepository.findByTransDateBetween(startDate, endDate);
    // }

    // @Override
    // public List<Transaction> findByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
    //     return transactionRepository.findByTransAmountBetween(minAmount, maxAmount);
    // }
}