package com.bankapp.model;

import java.math.BigDecimal;
import java.sql.Date;
import jakarta.persistence.*;

@Entity
@Table(name="Transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "trans_type",nullable = false)
    private String trans_type;
    
    @Column(name = "trans_amount",nullable = false)
    private BigDecimal trans_amount;

    @Column(name = "trans_date",nullable = false)
    private Date trans_date;

    public Transaction() {}

    public Transaction(User user,String trans_type,BigDecimal trans_amount,Date trans_date)
    {
        this.user=user;
        this.trans_amount=trans_amount;
        this.trans_type=trans_type;
        this.trans_date=trans_date;
    }
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTrans_type() {
        return trans_type;
    }

    public void setTrans_type(String trans_type) {
        this.trans_type = trans_type;
    }

    public BigDecimal getTrans_amount() {
        return trans_amount;
    }

    public Date getTrans_date() {
        return trans_date;
    }
}
