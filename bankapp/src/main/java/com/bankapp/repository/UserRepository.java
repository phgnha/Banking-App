package com.bankapp.repository;

import com.bankapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // For validLogin and basic checks
    Optional<User> findByUsernameAndPassword(String username, String password);
    
    // For checkUser method
    boolean existsByUsernameOrCCCDOrPhoneNumber(String username, String cccd, String phoneNumber);
    
    // For resetPassword
    Optional<User> findByCCCDAndPhoneNumber(String cccd, String phoneNumber);
    
    // For transfer
    Optional<User> findByUsername(String username);
    
    // For balance operations
    @Modifying
    @Query("UPDATE User u SET u.currentBalance = :balance WHERE u.id = :userId")
    void updateBalance(Long userId, BigDecimal balance);
    
    // For password reset
    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :userId")
    void updatePassword(Long userId, String newPassword);
    
    // For unique field checks
    boolean existsByUsername(String username);
    boolean existsBySTK(String stk);
    boolean existsByCCCD(String cccd);
    
    // For OTP validation
    Optional<User> findByIdAndPIN(Long id, String pin);
}