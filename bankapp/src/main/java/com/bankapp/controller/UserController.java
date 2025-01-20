package com.bankapp.controller;

import com.bankapp.model.User;
import com.bankapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            userService.register(user.getUsername(), user.getPassword(), user.getCCCD(), user.getPIN(), user.getPhoneNumber(), user.getSTK());
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String username, @RequestParam String password) {
        Optional<User> user = userService.validLogin(username, password);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(400).build());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String cccd, @RequestParam String phoneNumber, @RequestParam String newPassword) {
        boolean success = userService.resetPassword(cccd, phoneNumber, newPassword);
        return success ? ResponseEntity.ok("Password reset successfully") : ResponseEntity.badRequest().body("Failed to reset password");
    }

    @GetMapping("/{userId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long userId) {
        BigDecimal balance = userService.getBalance(userId);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/{userId}/transfer")
    public ResponseEntity<String> transfer(@PathVariable Long userId, @RequestParam String toUsername, @RequestParam BigDecimal amount) {
        boolean success = userService.transfer(userId, toUsername, amount);
        return success ? ResponseEntity.ok("Transfer successful") : ResponseEntity.badRequest().body("Transfer failed");
    }

    @PostMapping("/{userId}/update-balance")
    public ResponseEntity<String> updateBalance(@PathVariable Long userId, @RequestParam BigDecimal newBalance) {
        userService.updateBalance(userId, newBalance);
        return ResponseEntity.ok("Balance updated successfully");
    }
}