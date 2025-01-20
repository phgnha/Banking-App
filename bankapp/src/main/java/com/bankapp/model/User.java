package com.bankapp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name="Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name= "balance", precision = 10, scale = 2)
    private BigDecimal currentBalance;

    @Column(nullable = false, unique = true)
    private String CCCD;

    @Column(nullable = false)
    private String PIN;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String STK;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    public User(){}
    
    public User(long id, String username, String password, BigDecimal currentBalance,String CCCD,String PIN,String phoneNumer,String STK)
    {
        this.id=id;
        this.username=username;
        this.password=password;
        this.currentBalance=currentBalance;
        this.CCCD=CCCD;
        this.PIN=PIN;
        this.phoneNumber=phoneNumer;
        this.STK=STK;
    }
    public void setUsername(String username){
        this.username=username;
    }
    public void setSTK(String STK){
        this.STK=STK;
    }
    public void setCCCD(String CCCD){
        this.CCCD=CCCD;
    }
    public void setPIN(String pin){
        this.PIN=pin;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public void setPhoneNumber(String phonenumber){
        this.phoneNumber=phonenumber;
    }

    public String getSTK() {
        return STK;
    }
    public String getCCCD()
    {
        return this.CCCD;
    }
    public String getPIN() // đổi sang PIN
    {
        return this.PIN;
    }
    public long getId()
    {
        return this.id;
    }
    public String getUsername()
    {
        return this.username;
    }
    public String getPassword()
    {
        return this.password;
    }
    public BigDecimal getCurrentBalance()
    {
        return this.currentBalance;
    }
    public void setCurrentBalance(BigDecimal newBalance)
    {
        this.currentBalance=newBalance.setScale(2, RoundingMode.FLOOR);
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }
}
