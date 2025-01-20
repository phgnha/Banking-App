package com.bankapp.service;

import com.bankapp.model.User;
import com.bankapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void register(String username, String password, String cccd, 
                        String pin, String phoneNumber, String stk) throws Exception {
        if (checkUser(username, cccd, phoneNumber)) {
            throw new Exception("User already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCCCD(cccd);
        user.setPIN(pin);
        user.setPhoneNumber(phoneNumber);
        user.setSTK(stk);
        user.setCurrentBalance(BigDecimal.ZERO);
        
        userRepository.save(user);
    }

    @Override
    public Optional<User> validLogin(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public boolean resetPassword(String cccd, String phoneNumber, String newPassword) {
        Optional<User> user = userRepository.findByCCCDAndPhoneNumber(cccd, phoneNumber);
        if (user.isPresent()) {
            userRepository.updatePassword(user.get().getId(), newPassword);
            return true;
        }
        return false;
    }

    @Override
    public void updateBalance(Long userId, BigDecimal newBalance) {
        userRepository.updateBalance(userId, newBalance);
    }

    @Override
    public boolean checkOTP(Long userId, String otp) {
        return userRepository.findByIdAndPIN(userId, otp).isPresent();
    }

    @Override
    public boolean checkUser(String username, String cccd, String phoneNumber) {
        return userRepository.existsByUsernameOrCCCDOrPhoneNumber(username, cccd, phoneNumber);
    }

    @Override
    public Optional<User> findByUsername(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public BigDecimal getBalance(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getCurrentBalance).orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional
    public boolean transfer(Long fromUserId, String toUsername, BigDecimal amount) {
        Optional<User> fromUser = userRepository.findById(fromUserId);
        Optional<User> toUser = userRepository.findByUsername(toUsername);
        
        if (fromUser.isPresent() && toUser.isPresent()) {
            User from = fromUser.get();
            User to = toUser.get();
            
            if (from.getCurrentBalance().compareTo(amount) < 0) {
                return false;
            }
            
            from.setCurrentBalance(from.getCurrentBalance().subtract(amount));
            to.setCurrentBalance(to.getCurrentBalance().add(amount));
            
            userRepository.save(from);
            userRepository.save(to);
            
            return true;
        }
        return false;
    }
}