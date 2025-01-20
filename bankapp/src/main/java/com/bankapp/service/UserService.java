package com.bankapp.service;

import com.bankapp.model.User;
import java.math.BigDecimal;
import java.util.Optional;

public interface UserService {
    void register(String username, String password, String cccd, String pin, 
                 String phoneNumber, String stk) throws Exception;
    Optional<User> validLogin(String username, String password);
    boolean resetPassword(String cccd, String phoneNumber, String newPassword);
    void updateBalance(Long userId, BigDecimal newBalance);
    boolean checkOTP(Long userId, String otp);
    boolean checkUser(String username, String cccd, String phoneNumber);
    Optional<User> findByUsername(Long userId);
    BigDecimal getBalance(Long userId);
    boolean transfer(Long fromUserId, String toUsername, BigDecimal amount);
}