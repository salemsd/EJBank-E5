package com.ejbank.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "ejbank_account_type")
public class AccountType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "overdraft")
    private int overdraft;

    public AccountType() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }
    public int getOverdraft() { return overdraft; }
    public void setOverdraft(int overdraft) { this.overdraft = overdraft; }
}