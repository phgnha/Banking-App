package com.bankapp.controller;

import com.bankapp.model.Transaction;
import com.bankapp.model.User;
import com.bankapp.service.TransactionService;
import com.bankapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    // Get transaction history
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable Long userId) {
        Optional<User> user = userService.findByUsername(userId);
        if (user.isPresent()) {
            List<Transaction> transactions = transactionService.getPastTransactions(user.get());
            return ResponseEntity.ok(transactions);
        }
        return ResponseEntity.notFound().build();
    }

    // Add new transaction
    @PostMapping("/add")
    public ResponseEntity<String> addTransaction(@RequestBody Transaction transaction) {
        boolean success = transactionService.addTransaction(transaction);
        if (success) {
            return ResponseEntity.ok("Transaction added successfully");
        }
        return ResponseEntity.badRequest().body("Failed to add transaction");
    }

    // // Get transactions by date range
    // @GetMapping("/date-range")
    // public ResponseEntity<List<Transaction>> getTransactionsByDateRange(
    //         @RequestParam Date startDate,
    //         @RequestParam Date endDate) {
    //     List<Transaction> transactions = transactionService.findByDateRange(startDate, endDate);
    //     return ResponseEntity.ok(transactions);
    // } 

    // // Get transactions by amount range
    // @GetMapping("/amount-range")
    // public ResponseEntity<List<Transaction>> getTransactionsByAmountRange(
    //         @RequestParam BigDecimal minAmount,
    //         @RequestParam BigDecimal maxAmount) {
    //     List<Transaction> transactions = transactionService.findByAmountRange(minAmount, maxAmount);
    //     return ResponseEntity.ok(transactions);
    // }

    // // Get transactions by type
    // @GetMapping("/type/{transType}")
    // public ResponseEntity<List<Transaction>> getTransactionsByType(
    //         @PathVariable String transType) {
    //     List<Transaction> transactions = transactionService.findByTransType(transType);
    //     return ResponseEntity.ok(transactions);
    // }
}